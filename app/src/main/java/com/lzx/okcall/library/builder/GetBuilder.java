package com.lzx.okcall.library.builder;

import android.net.Uri;
import com.lzx.okcall.library.HttpMethod;
import com.lzx.okcall.library.Utils;
import com.lzx.okcall.library.call.OkHttpCall;

import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * 处理GET请求
 */
public class GetBuilder extends BaseRequestBuilder<GetBuilder> {
    private String requestUrl;
    private Map<String, Object> params;
    private boolean hasBody;
    private okhttp3.Call.Factory callFactory;

    public GetBuilder(String requestUrl, Map<String, Object> params, boolean hasBody, Call.Factory callFactory) {
        this.requestUrl = requestUrl;
        this.params = params;
        this.hasBody = hasBody;
        this.callFactory = callFactory;
    }

    public OkHttpCall build() {
        if (params != null) {
            requestUrl = appendParams(requestUrl, params);
        }
        RequestBuilder builder = new RequestBuilder(
                HttpMethod.GET,
                Utils.baseUrl(requestUrl),
                requestUrl,
                appendHeaders(),
                contentType,
                hasBody,
                isFormEncoded,
                isMultipart);
        return new OkHttpCall(builder, callFactory);
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
