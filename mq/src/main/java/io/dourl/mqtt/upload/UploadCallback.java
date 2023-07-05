package io.dourl.mqtt.upload;

import androidx.annotation.Nullable;

public interface UploadCallback {

    void onSuccess(String key);

    void onFail(@Nullable Exception e);

    void onProgress(double p);
}
