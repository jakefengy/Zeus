package com.xm.zeus;

import android.content.Intent;
import android.util.Log;

import com.xm.zeus.service.ZeusRemoteService;
import com.xm.zeus.volley.utils.NetApplication;

/**
 * Created by fengy on 2016-01-24.
 */
public class ZeusApplication extends NetApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, ZeusRemoteService.class));
        Log.i("RemoteMsgTag", "ZeusApplication.onCreate.startService");
    }
}
