package com.xm.zeus.volley.utils;

import android.app.Application;

/**
 * 作者：小孩子xm on 2016-02-16 22:06
 * 邮箱：1065885952@qq.com
 */
public class NetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestUtils.initRequestUtils(this);
    }
}
