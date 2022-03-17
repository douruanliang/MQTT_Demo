package io.dourl.http.model;

import java.io.Serializable;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/17
 */
public interface IResponse extends Serializable {
    /**
     * 业务逻辑上是否成功
     * @return true代表成功
     */
    boolean isSucceeded();

    /**
     * 获取业务逻辑出错代码
     * @return
     */
    int getErrorCode();

    /**
     * 获取出错信息
     * @return
     */
    String getErrorMessage();
}
