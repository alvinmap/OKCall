package com.lzx.okcall.library;

import com.lzx.okcall.library.builder.GetBuilder;
import com.lzx.okcall.library.builder.PostBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkCall {
    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    private static void initOkHttpClient() {
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

    public static GetBuilder get(String url, Map<String, Object> params) {
        String realUrl = handlerUrl(url);
        return new GetBuilder(realUrl, params, false, mOkHttpClient);
    }

    public void getWithToken() {

    }

    public static PostBuilder post(String url, Map<String, Object> params) {
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
    private static String handlerUrl(String url) {
        return url;
    }

}
