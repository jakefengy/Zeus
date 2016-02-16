package com.xm.zeus.volley.expand;

/**
 * @author fengy on 2016-02-16
 */
public enum RequestCallbackCode {

    NetworkUnavailable {
        public String getCode() {
            return "-1";
        }

        public String getName() {
            return "网络不可用";
        }
    };

    public abstract String getCode();

    public abstract String getName();

}
