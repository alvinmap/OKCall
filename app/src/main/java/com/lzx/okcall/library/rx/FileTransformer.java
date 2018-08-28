package com.lzx.okcall.library.rx;

import android.util.Log;

import com.lzx.okcall.library.info.DownloadInfo;
import com.lzx.okcall.library.info.Response;
import com.lzx.okcall.library.Utils;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * json 数据转换
 * create by lzx
 * time:2018/8/23
 */
public class FileTransformer implements ObservableTransformer<Response, DownloadInfo>, FlowableTransformer<Response, DownloadInfo>,
        SingleTransformer<Response, DownloadInfo>, MaybeTransformer<Response, DownloadInfo> {

    private String destFileDir;
    private String destFileName;

    public FileTransformer(String destFileDir, String destFileName) {
        Utils.checkNotNull(destFileDir, "destFileDir is null");
        Utils.checkNotNull(destFileName, "destFileName is null");
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    /**
     * 保存文件流
     */
    private DownloadInfo saveFile(Response response, String destFileDir, String destFileName, Observer<? super DownloadInfo> observer) throws IOException {
        ResponseBody value = response.body();
        //创建下载info
        final DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDestFileDir(destFileDir);
        downloadInfo.setDestFileName(destFileName);
        downloadInfo.setFile(null);
        if (value == null) {
            downloadInfo.setStatus(DownloadInfo.FAIL);
            return downloadInfo;
        }
        File dir = new File(destFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File output = new File(dir, destFileName);
        //下载总进度
        long contentLength = value.contentLength();
        downloadInfo.setDownloadTotalSize(contentLength);
        //读写文件
        BufferedSink sink = Okio.buffer(Okio.sink(output));
        Buffer buffer = sink.buffer();
        long total = 0;
        long len;
        int bufferSize = 200 * 1024; //200kb
        BufferedSource source = value.source();
        while ((len = source.read(buffer, bufferSize)) != -1) {
            sink.emit();
            total += len;
            downloadInfo.setDownloadProgress(total); //设置进度
            downloadInfo.setStatus(DownloadInfo.DOWNLOADING);
            observer.onNext(downloadInfo); //发射进度
        }
        //设置状态
        downloadInfo.setStatus(output.exists() ? DownloadInfo.SUCCESS : DownloadInfo.FAIL);
        source.close();
        sink.close();
        observer.onNext(downloadInfo); //下载完成发射最后一次进度
        return downloadInfo;
    }

    @Override
    public ObservableSource<DownloadInfo> apply(final Observable<Response> upstream) {
        return upstream.flatMap(new Function<Response, ObservableSource<DownloadInfo>>() {
            @Override
            public ObservableSource<DownloadInfo> apply(Response response) {
                return upstream.flatMap(new Function<Response, ObservableSource<DownloadInfo>>() {
                    @Override
                    public ObservableSource<DownloadInfo> apply(final Response response) {
                        return new ObservableSource<DownloadInfo>() {
                            @Override
                            public void subscribe(Observer<? super DownloadInfo> observer) {
                                try {
                                    saveFile(response, destFileDir, destFileName, observer); //保存文件
                                    observer.onComplete(); //发射完成
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    observer.onError(e); //发射错误
                                }
                            }
                        };
                    }
                });
            }
        });
    }

    @Override
    public Publisher<DownloadInfo> apply(Flowable<Response> upstream) {
        return null;
    }

    @Override
    public MaybeSource<DownloadInfo> apply(Maybe<Response> upstream) {
        return null;
    }

    @Override
    public SingleSource<DownloadInfo> apply(Single<Response> upstream) {
        return null;
    }
}


