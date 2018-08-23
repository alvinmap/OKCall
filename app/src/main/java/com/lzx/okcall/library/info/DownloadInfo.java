package com.lzx.okcall.library.info;

import java.io.File;

/**
 * 下载文件信息
 * create by lzx
 * time:2018/8/23
 */
public class DownloadInfo {
    private String status;
    private String destFileDir;
    private String destFileName;
    private File file;

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
}
