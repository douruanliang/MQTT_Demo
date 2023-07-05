package io.dourl.mqtt.upload;


import java.io.File;

import io.dourl.http.CallHandler;

public interface UploadInterface {

    void init();

    CallHandler doUpload(final File file, UpLoadParam param, final UploadCallback callback);

    void updateToken();
}
