package com.lzx.okcall;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lzx.okcall.library.OkCall;
import com.lzx.okcall.library.Response;
import com.lzx.okcall.library.call.BaseDataCallBack;
import com.lzx.okcall.library.call.Call;
import com.lzx.okcall.library.call.Callback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });

    }

    @SuppressLint("CheckResult")
    private void request() {
        String url = "https://www.easy-mock.com/mock/5b4c0d81a618510d7322b2f0/example/query";
        OkCall.injectCall()
                .get(url, null)
                .rxBuild()
                .map(new Function<String, LZX>() {
                    @Override
                    public LZX apply(String s) {
                        return new Gson().fromJson(s, LZX.class);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LZX>() {
                    @Override
                    public void accept(LZX lzx) throws Exception {
                        Log.i("xian", "json = " + lzx.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.i("xian", "throwable = " + throwable.getMessage());
                    }
                });
    }

    public class LZX {
        public String name;
        public String msg;

        @Override
        public String toString() {
            return "LZX{" +
                    "name='" + name + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
