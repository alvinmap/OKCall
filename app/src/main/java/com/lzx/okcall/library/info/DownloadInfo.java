package com.lzx.okcall.library.info;

import java.io.File;

/**
 * 下载文件信息
 * create by lzx
 * time:2018/8/23
 */
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDestFileDir() {
        return destFileDir;
    }

    public void setDestFileDir(String destFileDir) {
        this.destFileDir = destFileDir;
    }

    public String getDestFileName() {
        return destFileName;
    }

    public void setDestFileName(String destFileName) {
        this.destFileName = destFileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(long downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public long getDownloadTotalSize() {
        return downloadTotalSize;
    }

    public void setDownloadTotalSize(long downloadTotalSize) {
        this.downloadTotalSize = downloadTotalSize;
    }

    public float getDownloadPercent() {
        if (downloadTotalSize != 0) {
            return (float) downloadProgress / (float) downloadTotalSize;
        } else {
            return 0;
        }
    }
}
