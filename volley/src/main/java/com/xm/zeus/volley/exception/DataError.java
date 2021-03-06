package com.xm.zeus.volley.exception;

import com.xm.zeus.volley.source.VolleyError;

/**
 * 自定义异常
 *
 * @author 小孩子xm
 */
public class DataError extends VolleyError {

    private String errorCode;
    private String errorMsg;

    public DataError(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
