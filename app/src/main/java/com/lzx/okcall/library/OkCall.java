package com.lzx.okcall.library;

import com.lzx.okcall.library.builder.GetBuilder;
import com.lzx.okcall.library.builder.PostBuilder;

import java.util.Map;
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

    public GetBuilder get(String url, Map<String, Object> params) {
        String realUrl = handlerUrl(url);
        return new GetBuilder(realUrl, params, false, mOkHttpClient);
    }

    public void getWithToken() {

    }

    public PostBuilder post(String url, Map<String, Object> params) {
        String realUrl = handlerUrl(url);
        return new PostBuilder(realUrl, params, false, mOkHttpClient);
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

}
