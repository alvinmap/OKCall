package com.lzx.okcall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lzx.okcall.library.OkCall;
import com.lzx.okcall.library.Response;
import com.lzx.okcall.library.call.BaseDataCallBack;
import com.lzx.okcall.library.call.Call;
import com.lzx.okcall.library.call.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "1");
        String url = "https://www.easy-mock.com/mock/5b4c0d81a618510d7322b2f0/example/upload";
        OkCall.post(url, null).build()
                .enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            Log.i("xian", "result = " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i("xian", "IOException = " + e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("xian", "errorString = " + t.getMessage());
                    }
                });
//                .enqueue(new BaseDataCallBack<LZX>() {
//
//                    @Override
//                    public void onResponse(LZX result) {
//                        Log.i("xian", "result = " + result.toString());
//                    }
//
//                    @Override
//                    public void onFailure(String errorString) {
//                        Log.i("xian", "errorString = " + errorString);
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
