package com.lzx.okcall.library.analyze;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class StringConverter implements Converter<ResponseBody, String> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");


    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
