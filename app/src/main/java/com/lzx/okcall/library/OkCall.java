package com.lzx.okcall.library;

import com.lzx.okcall.library.builder.CallRequest;
import com.lzx.okcall.library.builder.RequestBuilder;
import com.lzx.okcall.library.info.DownloadInfo;
import com.lzx.okcall.library.rx.FileTransformer;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
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

    /**
     * 普通GET请求
     */
    public CallRequest get(String url, Map<String, Object> params) {
        String realUrl = handlerUrl(url);
        RequestBuilder requestBuilder = new RequestBuilder(Method.GET, realUrl, false, false, false);
        //组装参数
        if (params != null) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                requestBuilder.addQueryParam(key, String.valueOf(params.get(key)), false);
            }
        }
        return new CallRequest(mOkHttpClient, requestBuilder);
    }

    /**
     * 普通POST请求
     */
    public CallRequest post(String url, Map<String, Object> params) {
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
        return new CallRequest(mOkHttpClient, requestBuilder);
    }

    /**
     * POST string
     */
    public void postString() {

    }

    /**
     * POST file
     */
    public void postFile() {

    }

    /**
     * 下载
     */
    public Observable<DownloadInfo> rxDownload(String downloadUrl, String destFileDir, String destFileName) {
        return get(downloadUrl, null)
                .rxBuild()
                .compose(new FileTransformer(destFileDir, destFileName));
    }

    /**
     * 上传
     */
    public void upload() {

    }


    /**
     * 处理url
     */
    private String handlerUrl(String url) {
        return url;
    }

    /**
     * 请求类型
     */
    public interface Method {
        String GET = "GET";
        String POST = "POST";
        String DELETE = "DELETE";
        String PUT = "PUT";
    }

}
