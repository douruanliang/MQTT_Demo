package io.dourl.http.model;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/3/17
 */
public class BaseResponse implements IResponse {
    public String message;
    public int code = 0;

    @Override
    public boolean isSucceeded() {
        return code == 0;
    }

    @Override
    public int getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
