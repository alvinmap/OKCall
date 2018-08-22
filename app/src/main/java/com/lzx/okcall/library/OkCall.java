package com.lzx.okcall.library;

import com.lzx.okcall.library.builder.GetBuilder;
import com.lzx.okcall.library.builder.PostBuilder;
import com.lzx.okcall.library.builder.RequestBuilder;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 简单封装OKHttp的请求框架
 */
public class OkCall {
    private OkHttpClient mOkHttpClient;

    public static OkCall injectCall() {
        return OkCallSingleton.get();
    }

    private static final Singleton<OkCall> OkCallSingleton = new Singleton<OkCall>() {
        @Override
        protected OkCall create() {
            return new OkCall();
        }
    };

    private OkCall() {
        initOkHttpClient();
    }

    private void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (OkCall.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public GetBuilder get(String url, Map<String, Object> params) {
        String realUrl = handlerUrl(url);
        RequestBuilder requestBuilder = new RequestBuilder(Method.GET, realUrl, false, false, false);
        //组装参数
        if (params != null) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                requestBuilder.addQueryParam(key, String.valueOf(params.get(key)), false);
            }
        }
        return new GetBuilder(realUrl, params, mOkHttpClient, requestBuilder);
    }


    public PostBuilder post(String url, Map<String, Object> params) {
        String realUrl = handlerUrl(url);
        boolean hasBody = false;
        boolean isFormEncoded = false;
        if (params == null) {
            hasBody = true;
        } else {
            isFormEncoded = true;
        }
        RequestBuilder requestBuilder = new RequestBuilder(Method.POST, realUrl, hasBody, isFormEncoded, false);
        //组装参数
        if (params != null) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                requestBuilder.addFormField(key, String.valueOf(params.get(key)), false);
            }
        }
        return new PostBuilder(realUrl, params, mOkHttpClient, requestBuilder);
    }

    public void postString() {

    }

    public void postFile() {

    }

    /**
     * 处理url
     */
    private String handlerUrl(String url) {
        return url;
    }

    public interface Method {
        String GET = "GET";
        String POST = "POST";
        String DELETE = "DELETE";
        String PUT = "PUT";
    }

}
