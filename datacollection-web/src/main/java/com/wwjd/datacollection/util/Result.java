package com.wwjd.datacollection.util;

/**
 * result
 *
 * @author adao
 * @CopyRight qtshe
 * @Created 2019-02-11 15:34:00
 */
public final class Result<T> {
    /**
     * code  4000 is success
     */
    private int code;
    /**
     * is ok
     */
    private boolean success;
    /**
     * return message
     */
    private String msg;
    /**
     * return data
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
