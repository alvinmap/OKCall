package com.lzx.okcall.library.builder;

import android.net.Uri;
import android.util.Log;


import com.lzx.okcall.library.HttpMethod;
import com.lzx.okcall.library.Utils;
import com.lzx.okcall.library.analyze.Converter;
import com.lzx.okcall.library.call.OkHttpCall;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class GetBuilder<T> {
    private String requestUrl;
    private Map<String, Object> params;
    private boolean hasBody;
    private okhttp3.Call.Factory callFactory;
    private Converter<ResponseBody, T> responseConverter;
    private Converter.Factory mConverterFactory;
    private Map<String, String> headers;
    private MediaType contentType;
    private boolean isFormEncoded = false;
    private boolean isMultipart = false;


    public GetBuilder(String requestUrl, Map<String, Object> params, boolean hasBody, Call.Factory callFactory, Converter.Factory converterFactory) {
        this.requestUrl = requestUrl;
        this.params = params;
        this.hasBody = hasBody;
        this.callFactory = callFactory;
        mConverterFactory = converterFactory;

        Type type = callType();
        Log.i("xian", "-->" + type);
        responseConverter = (Converter<ResponseBody, T>) mConverterFactory.requestStringConverter();
    }

    public GetBuilder<T> headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public GetBuilder<T> addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

    public GetBuilder<T> mediaType(MediaType mediaType) {
        this.contentType = mediaType;
        return this;
    }

    public GetBuilder<T> setFormEncoded(boolean formEncoded) {
        isFormEncoded = formEncoded;
        return this;
    }

    public GetBuilder<T> setMultipart(boolean multipart) {
        isMultipart = multipart;
        return this;
    }

    public OkHttpCall<T> build() {
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
        return new OkHttpCall<>(builder, callFactory, responseConverter);
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

    private Type callType() {
        boolean isResult = false;
        boolean isBody = false;
        Type returnType = this.getClass().getGenericSuperclass();
//        Type responseType;
//        Type observableType = Utils.getParameterUpperBound(0, (ParameterizedType) returnType);
//        Class<?> rawObservableType = Utils.getRawType(observableType);
//        if (rawObservableType == Response.class) {
//            if (!(observableType instanceof ParameterizedType)) {
//                throw new IllegalStateException("Response must be parameterized"
//                        + " as Response<Foo> or Response<? extends Foo>");
//            }
//            responseType = Utils.getParameterUpperBound(0, (ParameterizedType) observableType);
//        } else {
//            responseType = observableType;
//            isBody = true;
//        }
        if (returnType instanceof ParameterizedType) {
            Log.i("xian", "草泥马");
        } else if (returnType instanceof TypeVariable) {
            Log.i("xian", "草泥马111");
        } else if (returnType instanceof WildcardType) {
            Log.i("xian", "草泥马222");
        } else if (returnType instanceof GenericArrayType) {
            Log.i("xian", "草泥马333");
        } else {
            Log.i("xian", "日狗了");
        }
        return returnType;
    }
}
