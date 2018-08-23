package com.lzx.okcall.library.builder;

import com.lzx.okcall.library.Response;
import com.lzx.okcall.library.call.OkHttpCall;
import com.lzx.okcall.library.rx.CallExecuteObservable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Call;

/**
 * 处理GET请求
 */
public class GetBuilder extends BaseRequestBuilder<GetBuilder> {

    private okhttp3.Call.Factory callFactory;
    private RequestBuilder requestBuilder;

    public GetBuilder(Call.Factory callFactory, RequestBuilder requestBuilder) {
        this.callFactory = callFactory;
        this.requestBuilder = requestBuilder;
    }

    @Override
    public OkHttpCall build() {
        if (headers != null) {
            requestBuilder.setHeaders(appendHeaders());
        }
        if (contentType != null) {
            requestBuilder.setContentType(contentType);
        }
        requestBuilder.createBuilder();

        return new OkHttpCall(requestBuilder, callFactory);
    }

    private Observable<Response> createObservable() {
        if (headers != null) {
            requestBuilder.setHeaders(appendHeaders());
        }
        if (contentType != null) {
            requestBuilder.setContentType(contentType);
        }
        requestBuilder.createBuilder();
        return new CallExecuteObservable(new OkHttpCall(requestBuilder, callFactory));
    }

    public Observable<Response> rxBuild() {
        Observable<Response> observable = createObservable();
        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }
        return observable;
    }

    public Flowable<Response> rxBuildFlowable() {
        Observable<Response> observable = createObservable();
        return observable.toFlowable(BackpressureStrategy.LATEST);
    }

    public Single<Response> rxBuildSingle() {
        Observable<Response> observable = createObservable();
        return observable.singleOrError();
    }

    public Maybe<Response> rxBuildMaybe() {
        Observable<Response> observable = createObservable();
        return observable.singleElement();
    }





}
