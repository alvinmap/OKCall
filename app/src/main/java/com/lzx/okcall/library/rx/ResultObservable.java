package com.lzx.okcall.library.rx;

import com.lzx.okcall.library.Response;

import java.io.IOException;

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
public class ResultObservable extends Observable<String> {

    private Observable<Response> upstream;

    public ResultObservable(Observable<Response> upstream) {
        this.upstream = upstream;
    }

    @Override
    protected void subscribeActual(final Observer<? super String> observer) {
        upstream.subscribe(new Observer<Response>() {
            @Override
            public void onSubscribe(Disposable d) {
                observer.onSubscribe(d);
            }

            @Override
            public void onNext(Response response) {
                try {
                    String json = response.body().string();
                    observer.onNext(json);
                } catch (IOException e) {
                    e.printStackTrace();
                    observer.onError(e);
                    observer.onComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    observer.onError(e);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(e, inner));
                }
                observer.onComplete();
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        });
    }
}
