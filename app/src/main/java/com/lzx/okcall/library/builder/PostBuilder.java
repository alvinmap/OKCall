package com.lzx.okcall.library.builder;

import android.net.Uri;

import com.lzx.okcall.library.HttpMethod;
import com.lzx.okcall.library.Utils;
import com.lzx.okcall.library.call.OkHttpCall;

import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * 处理POST请求
 */
public class PostBuilder extends BaseRequestBuilder<PostBuilder> {
    private String requestUrl;
    private Map<String, Object> params;
    private boolean hasBody;
    private okhttp3.Call.Factory callFactory;


    public PostBuilder(String requestUrl, Map<String, Object> params, boolean hasBody, Call.Factory callFactory) {
        this.requestUrl = requestUrl;
        this.params = params;
        this.hasBody = hasBody;
        this.callFactory = callFactory;
    }

    @Override
    public OkHttpCall build() {
        isFormEncoded = params != null;
        if (!isFormEncoded) {
            hasBody = true;
        }
        RequestBuilder builder = new RequestBuilder(
                HttpMethod.POST,
                Utils.baseUrl(requestUrl),
                requestUrl,
                appendHeaders(),
                contentType,
                hasBody,
                isFormEncoded,
                isMultipart);

        //添加post参数
        if (isFormEncoded) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder.addFormField(key, String.valueOf(params.get(key)), false);
            }
        }

        mOkHttpCall = new OkHttpCall(builder, callFactory);
        return mOkHttpCall;
    }

    @Override
    public void cancel() {
        if (mOkHttpCall != null && !mOkHttpCall.isCanceled()) {
            mOkHttpCall.cancel();
        }
    }

}
