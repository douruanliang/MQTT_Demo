package io.dourl.mqtt.thread;

/**
 * 带有成功失败回调的Runnable
 * Created by dourl on 16/5/16.
 */
public abstract class CallbackRunnable implements Runnable {

    private Callback mCallback;

    public CallbackRunnable(Callback callback) {
        mCallback = callback;
    }

    public void onSuccess() {
        if (mCallback != null) {
            mCallback.onSuccess();
        }
    }

    protected void onFail(Throwable e) {
        if (mCallback != null) {
            mCallback.onFail(e);
        }
    }

    @Override
    public abstract void run();

    public interface Callback {
        void onSuccess();

        void onFail(Throwable e);
    }
}
