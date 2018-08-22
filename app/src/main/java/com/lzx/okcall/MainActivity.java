package com.lzx.okcall;

import android.content.ContentProvider;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lzx.okcall.library.OkCall;
import com.lzx.okcall.library.Response;
import com.lzx.okcall.library.call.BaseDataCallBack;
import com.lzx.okcall.library.call.Call;
import com.lzx.okcall.library.call.Callback;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkCall.injectCall().getOkHttpClient().dispatcher().executorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "https://www.easy-mock.com/mock/5b4c0d81a618510d7322b2f0/example/query";
                    Response response = OkCall.injectCall().get(url, null).build().execute();
                    Log.i("xian", "result = " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("xian", "result = " + e.getMessage());
                }
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String url = "https://www.easy-mock.com/mock/5b4c0d81a618510d7322b2f0/example/query";
//                    Response response = OkCall.injectCall().get(url, null).build().execute();
//                    Log.i("xian", "result = " + response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.i("xian", "result = " + e.getMessage());
//                }
//            }
//        }).start();

//                .enqueue(new Callback() {
//                    @Override
//                    public void onResponse(Call call, Response response) {
//                        try {
//                            Log.i("xian", "result = " + response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.i("xian", "IOException = " + e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call call, Throwable t) {
//                        Log.i("xian", "errorString = " + t.getMessage());
//                    }
//                });
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
