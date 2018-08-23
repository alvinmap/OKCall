package com.lzx.okcall.library.rx;

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

    private File saveFile(Response response, String destFileDir, String destFileName) throws IOException {
        ResponseBody value = response.body();
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = value.byteStream();
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);

            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            value.close();
            if (is != null) is.close();
            if (fos != null) fos.close();
        }
    }

    @Override
    public ObservableSource<DownloadInfo> apply(final Observable<Response> upstream) {
        return upstream.flatMap(new Function<Response, ObservableSource<DownloadInfo>>() {
            @Override
            public ObservableSource<DownloadInfo> apply(Response response) throws Exception {
                return upstream.flatMap(new Function<Response, ObservableSource<DownloadInfo>>() {
                    @Override
                    public ObservableSource<DownloadInfo> apply(Response response) throws Exception {
                        File file = saveFile(response, destFileDir, destFileName);
                        final DownloadInfo downloadInfo = new DownloadInfo();
                        downloadInfo.setStatus(file.exists() ? "success" : "fail");
                        downloadInfo.setDestFileDir(destFileDir);
                        downloadInfo.setDestFileName(destFileName);
                        downloadInfo.setFile(file);
                        return new ObservableSource<DownloadInfo>() {
                            @Override
                            public void subscribe(Observer<? super DownloadInfo> observer) {
                                observer.onNext(downloadInfo);
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


