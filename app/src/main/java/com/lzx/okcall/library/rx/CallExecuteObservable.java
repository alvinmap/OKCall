package com.lzx.okcall.library.rx;

import com.lzx.okcall.library.info.Response;
import com.lzx.okcall.library.call.Call;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * create by lzx
 * time:2018/8/22
 */
public class CallExecuteObservable extends Observable<Response> {
    private Call originalCall;

    public CallExecuteObservable(Call originalCall) {
        this.originalCall = originalCall;
    }

    @Override
    protected void subscribeActual(Observer<? super Response> observer) {
        Call call = originalCall.clone();
        observer.onSubscribe(new CallDisposable(call));

        boolean terminated = false;
        try {
            Response response = call.execute(); //请求
            if (!call.isCanceled()) {
                observer.onNext(response);
            }
            if (!call.isCanceled()) {
                terminated = true;
                observer.onComplete();
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            if (terminated) {
                RxJavaPlugins.onError(t);
            } else if (!call.isCanceled()) {
                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }
        }
    }

    private static final class CallDisposable implements Disposable {
        private final Call call;

        CallDisposable(Call call) {
            this.call = call;
        }

        @Override
        public void dispose() {
            call.cancel();
        }

        @Override
        public boolean isDisposed() {
            return call.isCanceled();
        }
    }
}
