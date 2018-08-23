package com.lzx.okcall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lzx.okcall.library.OkCall;
import com.lzx.okcall.library.Response;
import com.lzx.okcall.library.rx.ResultTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {


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
                .compose(new ResultTransformer<>(LZX.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LZX>() {
                    @Override
                    public void accept(LZX json) {
                        Log.i("MainActivity", "json = " + json.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.i("MainActivity", "throwable = " + throwable.getMessage());
                    }
                });

//        OkCall.injectCall()
//                .get(url, null)
//                .rxBuild()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Response>() {
//                    @Override
//                    public void accept(Response response) throws Exception {
//                        Log.i("MainActivity", "json = " + response.body().string());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.i("MainActivity", "Throwable = " + throwable.getMessage());
//                    }
//                });
//
//
//        OkCall.injectCall().get(url, null)
//                .build()
//                .enqueue(new Callback() {
//                    @Override
//                    public void onResponse(Call call, Response response) {
//                        try {
//                            Log.i("MainActivity", "json = " + response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call call, Throwable t) {
//                        Log.i("MainActivity", "Throwable = " + t.getMessage());
//                    }
//                });
//
//        OkCall.injectCall().get(url, null)
//                .build()
//                .enqueue(new BaseDataCallBack<LZX>() {
//                    @Override
//                    public void onResponse(LZX result) {
//                        Log.i("MainActivity", "result = " + result.toString());
//                    }
//
//                    @Override
//                    public void onFailure(String errorString) {
//                        Log.i("MainActivity", "onFailure = " + errorString);
//                    }
//                });
    }

    public static class LZX {
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
