package io.dourl.mqtt.upload;


public class UpLoadParam {
    public UploadType type;
    public int w = 0;
    public int h = 0;
    public int d = 0;

    public UpLoadParam(UploadType type) {
        this.type = type;
    }

    public UpLoadParam(UploadType type, int w, int h) {
        this.type = type;
        this.w = w;
        this.h = h;
    }
}
