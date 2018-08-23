package com.lzx.okcall.library.call;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lzx.okcall.library.info.Response;

import okhttp3.ResponseBody;

import static com.lzx.okcall.library.Utils.getSuperClassGenericType;

/**
 * 基础回调封装,返回解析后结果
 * @param <T> 实体类l
 */
public abstract class BaseDataCallBack<T> implements Callback {

    private Gson gson;
    private TypeAdapter<T> adapter;
    private Class<T> persistentClass;

    protected BaseDataCallBack() {
        if (this.gson == null) {
            this.gson = new Gson();
        }
    }

    @Override
    public void onResponse(Call call, Response response) {
        this.persistentClass = (Class<T>) getSuperClassGenericType(getClass(), 0);

        ResponseBody value = response.body();
        if (value == null) {
            return;
        }
        try {
            adapter = gson.getAdapter(TypeToken.get(persistentClass));
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            T result = adapter.read(jsonReader);
            if (result != null) {
                onResponse(result);
            } else {
                onFailure("request result is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(e.getMessage());
        } finally {
            value.close();
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        onFailure(t.getMessage());
    }

    public abstract void onResponse(T result);

    public abstract void onFailure(String errorString);
}
