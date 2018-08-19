package com.lzx.okcall.library;



import com.lzx.okcall.library.analyze.GsonConverterFactory;
import com.lzx.okcall.library.builder.GetBuilder;

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

    public static <T> GetBuilder<T> get(String url, Map<String, Object> params) {
        String realUrl = handlerUrl(url);
        GetBuilder<T> builder = new GetBuilder<>(realUrl, params, false, mOkHttpClient, GsonConverterFactory.create());
        return builder;
    }

    public void getWithToken() {

    }

    /**
     * 处理url
     */
    private static String handlerUrl(String url) {
        return url;
    }

}
