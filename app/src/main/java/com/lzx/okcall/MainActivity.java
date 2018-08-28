package com.lzx.okcall;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.lzx.okcall.library.OkCall;
import com.lzx.okcall.library.info.DownloadInfo;
import com.lzx.okcall.library.rx.ResultTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.security.Permission;
import java.security.Permissions;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"}, 1000);
        }
    }


    @SuppressLint("CheckResult")
    private void request() {
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1535452255376&di=d901dcd43dd615dab51203b617e76ad5&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F-Po3dSag_xI4khGko9WTAnF6hhy%2Flvpics%2Fh%3D800%2Fsign%3D3a5bf403d043ad4bb92e4bc0b2035a89%2Fa8014c086e061d95e9133e9e7af40ad162d9ca3b.jpg";
        OkCall.injectCall()
                .rxDownload(url, Environment.getExternalStorageDirectory().getPath() + "/11111", "meinv.jpg")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DownloadInfo downloadInfo) {
                        if (downloadInfo.getDownloadPercent() == 1) {
                            Log.i("xian", "===下载完成===");
                        } else {
                            Log.i("xian", "进度 = " + downloadInfo.getDownloadPercent() + " progress = " + downloadInfo.getDownloadProgress() + "  total = " + downloadInfo.getDownloadTotalSize());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("xian", "throwable = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("xian", " = 下载完成 = ");
                    }
                });
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
