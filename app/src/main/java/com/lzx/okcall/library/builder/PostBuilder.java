package com.lzx.okcall.library.builder;

import com.lzx.okcall.library.Utils;
import com.lzx.okcall.library.call.OkHttpCall;

import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.internal.http.HttpMethod;

/**
 * 处理POST请求
 */
public class PostBuilder extends BaseRequestBuilder<PostBuilder> {
    private String requestUrl;
    private Map<String, Object> params;

    private okhttp3.Call.Factory callFactory;
    private RequestBuilder requestBuilder;

    public PostBuilder(String requestUrl, Map<String, Object> params, Call.Factory callFactory, RequestBuilder requestBuilder) {
        this.requestUrl = requestUrl;
        this.params = params;
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
        mOkHttpCall = new OkHttpCall(requestBuilder, callFactory);
        return mOkHttpCall;
    }

    @Override
    public void cancel() {
        if (mOkHttpCall != null && !mOkHttpCall.isCanceled()) {
            mOkHttpCall.cancel();
        }
    }

}
