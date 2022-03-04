package io.dourl.mqtt.storage;

/**
 * Created by zhangpeng on 2018/1/31.
 * 数据库操作的回调
 */

public interface DbCallback<T> {
    void onSuccess(T t);

    void onFail(Throwable e);
}
