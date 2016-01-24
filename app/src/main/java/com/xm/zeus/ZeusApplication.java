package com.xm.zeus;

import android.app.Application;

import com.xm.zeus.volley.utils.RequestUtils;

/**
 * Created by fengy on 2016-01-24.
 */
public class ZeusApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestUtils.initRequestUtils(this);
    }
}
