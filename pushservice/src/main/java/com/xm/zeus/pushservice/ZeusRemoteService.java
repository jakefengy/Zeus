package com.xm.zeus.pushservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.xm.zeus.common.Logger;
import com.xm.zeus.pushservice.manager.BinderManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author fengy on 2016-02-18
 */
public class ZeusRemoteService extends Service {

    static final String TAG = "RemoteServiceTag";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    // Keys
    public final static String BinderTag = "BinderTag";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        String binderTag = intent.getStringExtra(BinderTag);
        if (TextUtils.isEmpty(binderTag)) {
            print("Service.onBind, bundle.getString(BinderTag) = null");
            return null;
        }
        print("Service.onBind, bundle.getString(BinderTag) = " + binderTag);
        return BinderManager.getInstance().getBinderByTag(binderTag);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        print("Service.onCreate");
        new Thread(new ExitService()).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        print("Service.onStartCommand，Default");
        if (intent != null) {
            print("Service.onStartCommand with intent from = " + intent.getStringExtra("StartTag"));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        print("Service.onDestroy");
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private class ExitService implements Runnable {
        @Override
        public void run() {

            int index = 0;
            while (!mIsServiceDestroyed.get()) {
//                print("--------- timeline = " + index);
                BinderManager.getInstance().notifyNewMsg("--------- timeline = " + index);
                index++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    protected void print(String content) {
        Log.i(TAG, content);

        try {
            Logger.writeLog(TAG + " : " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
