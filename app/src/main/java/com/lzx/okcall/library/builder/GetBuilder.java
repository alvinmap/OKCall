package com.lzx.okcall.library.builder;

import android.net.Uri;

import com.lzx.okcall.library.call.OkHttpCall;

import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.internal.http.HttpMethod;

/**
 * 处理GET请求
 */
public class GetBuilder extends BaseRequestBuilder<GetBuilder> {
    private String requestUrl;
    private Map<String, Object> params;
    private boolean hasBody = false;
    private okhttp3.Call.Factory callFactory;
    private RequestBuilder requestBuilder;

    public GetBuilder(String requestUrl, Map<String, Object> params, Call.Factory callFactory, RequestBuilder requestBuilder) {
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
        if (contentType!=null){
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

    private String appendParams(String url, Map<String, Object> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, String.valueOf(params.get(key)));
        }
        return builder.build().toString();
    }


}
