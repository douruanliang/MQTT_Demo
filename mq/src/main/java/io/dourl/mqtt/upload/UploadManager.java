package io.dourl.mqtt.upload;

import java.io.File;

import io.dourl.http.CallHandler;


public class UploadManager {
    private static UploadManager mInstance;

    public static UploadManager getInstance() {
        if (mInstance == null) {
            synchronized (UploadManager.class) {
                if (mInstance == null) {
                    mInstance = new UploadManager();
                }
            }
        }
        return mInstance;
    }

    private UploadInterface mUploadInterface;

    private UploadManager() {
    }

    public void init(UploadInterface uploadInterface) {
        this.mUploadInterface = uploadInterface;
        this.mUploadInterface.init();
    }

    public CallHandler uploadFile(File file, UpLoadParam param, final UploadCallback callback) {
        return mUploadInterface.doUpload(file, param, callback);
    }

    public void updateToKen() {
        mUploadInterface.updateToken();
    }
} 