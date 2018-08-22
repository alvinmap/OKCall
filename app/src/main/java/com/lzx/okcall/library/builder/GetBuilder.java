package com.lzx.okcall.library.builder;

import android.net.Uri;

import com.lzx.okcall.library.Response;
import com.lzx.okcall.library.call.OkHttpCall;
import com.lzx.okcall.library.rx.CallExecuteObservable;
import com.lzx.okcall.library.rx.ResultObservable;

import java.util.Map;
import java.util.Set;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.internal.http.HttpMethod;

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

    private Observable<String> createObservable() {
        if (headers != null) {
            requestBuilder.setHeaders(appendHeaders());
        }
        if (contentType != null) {
            requestBuilder.setContentType(contentType);
        }
        requestBuilder.createBuilder();
        Observable<Response> responseObservable = new CallExecuteObservable(new OkHttpCall(requestBuilder, callFactory));
        return new ResultObservable(responseObservable);
    }

    public Observable<String> rxBuild() {
        Observable<String> observable = createObservable();
        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }
        return observable;
    }

    public Flowable<String> rxBuildFlowable() {
        Observable<String> observable = createObservable();
        return observable.toFlowable(BackpressureStrategy.LATEST);
    }

    public Single<String> rxBuildSingle() {
        Observable<String> observable = createObservable();
        return observable.singleOrError();
    }

    public Maybe<String> rxBuildMaybe() {
        Observable<String> observable = createObservable();
        return observable.singleElement();
    }

    public Completable rxBuildCompletable() {
        Observable<String> observable = createObservable();
        return observable.ignoreElements();
    }

}
