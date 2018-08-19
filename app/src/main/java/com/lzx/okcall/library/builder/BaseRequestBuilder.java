package com.lzx.okcall.library.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * 基础builder
 */
public abstract class BaseRequestBuilder<T> {
    Map<String, String> headers;
    MediaType contentType;
    boolean isFormEncoded = false;
    boolean isMultipart = false;

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return (T) this;
    }

    public T mediaType(MediaType mediaType) {
        this.contentType = mediaType;
        return (T) this;
    }

    public T setFormEncoded(boolean formEncoded) {
        isFormEncoded = formEncoded;
        return (T) this;
    }

    public T setMultipart(boolean multipart) {
        isMultipart = multipart;
        return (T) this;
    }

    protected Headers appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        return headerBuilder.build();
    }
}