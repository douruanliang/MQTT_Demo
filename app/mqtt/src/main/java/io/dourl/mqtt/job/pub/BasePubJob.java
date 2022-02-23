package io.dourl.mqtt.job.pub;

public interface BasePubJob {

    void onAdded();

    boolean onRun() throws Throwable;
}
