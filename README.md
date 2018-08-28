# OKCall

简单封装了 OkHttp 的请求框架，可以结合 RxJava 一起使用。

## 用法

### 普通GET请求

GET 请求有两个参数，第一个是请求 URL,第二个是请求参数，如果没有则传 null。

1. 普通回调

```java
String url = "https://www.easy-mock.com/mock/5b4c0d81a618510d7322b2f0/example/query";
OkCall.injectCall()
        .get(url, null)
        .build()
        .enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    Log.i("MainActivity", "json = " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("MainActivity", "Throwable = " + t.getMessage());
            }
        });
```

2. 结合 Gson 做数据解析的回调

```java
String url = "https://www.easy-mock.com/mock/5b4c0d81a618510d7322b2f0/example/query";
OkCall.injectCall()
        .get(url, null)
        .build()
        .enqueue(new BaseDataCallBack<LZX>() {
            @Override
            public void onResponse(LZX result) {
                Log.i("MainActivity", "result = " + result.toString());
            }

            @Override
            public void onFailure(String errorString) {
                Log.i("MainActivity", "onFailure = " + errorString);
            }
        });

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
```

### GET 请求结合 RxJava 使用
结合 RxJava 只需要把 build 方法换成 rxBuild 方法即可，其他都一样。

1. 普通回调

```java
String url = "https://www.easy-mock.com/mock/5b4c0d81a618510d7322b2f0/example/query";
OkCall.injectCall()
        .get(url, null)
        .rxBuild()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Response>() {
            @Override
            public void accept(Response response) throws Exception {
                Log.i("MainActivity", "json = " + response.body().string());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i("MainActivity", "Throwable = " + throwable.getMessage());
            }
        });
``` 

2. 带数据解析

```java
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
```
只需要使用 compose ，new 一个 ResultTransformer 对象传入要解析成的实体类的 class 即可。

rxBuild 返回的是 Observable，如果你想返回 Flowable，则用 rxBuildFlowable 即可，同样的，如果想返回 Single，则用 rxBuildSingle，
想返回 Maybe，则用 rxBuildMaybe

### 普通POST请求
POST 请求跟 GET 请求用法一样，只是方法名改成 post 即可。

### RxJava 下载
```java
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
            public void onNext(DownloadInfo info) {
                if (info.getDownloadPercent() == 1) {
                    Log.i("xian", "===下载完成===");
                } else {
                    Log.i("xian", "进度 = " + info.getDownloadPercent() + " progress = " + info.getDownloadProgress() + "  total = " + info.getDownloadTotalSize());
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
```

RxJava 下载调用 rxDownload 方法，参数有三个，下载url，文件保存路径，保存文件名。回调信息为 DownloadInfo , 字段如下：

```java
public class DownloadInfo {
    public static final String SUCCESS = "success"; //成功
    public static final String FAIL = "fail"; //失败
    public static final String DOWNLOADING = "downloading"; //下载中

    private String status; //下载状态：SUCCESS,FAIL,DOWNLOADING
    private String destFileDir; //保存路径
    private String destFileName; //保存文件名
    private File file; //file对象，下载失败的时候为null
    private long downloadProgress = 0; //当前下载进度
    private long downloadTotalSize = 0; //总大小
    ...
}
```

onNext(DownloadInfo info) 回调中会不断回调下载进度直到下载完成为止，所以可以通过 info.getDownloadPercent() 是否等于 1，或者 info.getStatus() 是否等于 DownloadInfo.SUCCESS 来判断是否下载完成。