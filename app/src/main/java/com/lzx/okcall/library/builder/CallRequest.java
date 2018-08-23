package com.lzx.okcall.library.builder;

import com.lzx.okcall.library.info.Response;
import com.lzx.okcall.library.call.OkHttpCall;
import com.lzx.okcall.library.rx.CallExecuteObservable;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * create by lzx
 * time:2018/8/23
 */
public class CallRequest {
    private okhttp3.Call.Factory callFactory;
    private RequestBuilder requestBuilder;
    Map<String, String> headers;
    MediaType contentType;
    Scheduler scheduler;

    public CallRequest(Call.Factory callFactory, RequestBuilder requestBuilder) {
        this.callFactory = callFactory;
        this.requestBuilder = requestBuilder;
    }

    public CallRequest headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public CallRequest addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

    public CallRequest mediaType(MediaType mediaType) {
        this.contentType = mediaType;
        return this;
    }

    public CallRequest setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

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

    private Headers appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        return headerBuilder.build();
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


}
