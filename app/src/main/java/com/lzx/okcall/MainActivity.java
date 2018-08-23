package com.lzx.okcall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.lzx.okcall.library.OkCall;
import com.lzx.okcall.library.info.DownloadInfo;
import com.lzx.okcall.library.rx.ResultTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

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
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1535032435549&di=b32d355da356dab9c8dc1e6a6b714c52&imgtype=0&src=http%3A%2F%2Fs1.sinaimg.cn%2Fmw690%2F006amcbggy6Xu0MS22sf0%26690";
        OkCall.injectCall()
                .rxDownload(url, Environment.getExternalStorageDirectory().getPath() + "/11111", "meinv.jpg")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) throws Exception {
                        Log.i("MainActivity", "downloadInfo = " + downloadInfo.getStatus());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MainActivity", "throwable = " + throwable.getMessage());
                    }
                });

//        OkCall.injectCall()
//                .get(url, null)
//                .rxBuild()
//                .compose(new ResultTransformer<>(LZX.class))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<LZX>() {
//                    @Override
//                    public void accept(LZX json) {
//                        Log.i("MainActivity", "json = " + json.toString());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        Log.i("MainActivity", "throwable = " + throwable.getMessage());
//                    }
//                });

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
