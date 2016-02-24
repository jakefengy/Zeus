package com.xm.zeus.pushservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.xm.zeus.pushservice.manager.BinderManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author fengy on 2016-02-18
 */
public class ZeusRemoteService extends Service {

    static final String TAG = "RemoteServiceTag";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
    private Handler handler = new Handler();

    // Keys
    public final static String BinderTag = "BinderTag";

    // BinderManager
    private BinderManager binderManager;

    private IBinder msgBinder = new IMsgManager.Stub() {
        @Override
        public List<String> getMsgs() throws RemoteException {
            return null;
        }

        @Override
        public void registerListener(IMsgListener listener) throws RemoteException {

        }

        @Override
        public void unregisterListener(IMsgListener listener) throws RemoteException {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        String binderTag = intent.getStringExtra(BinderTag);
        if (TextUtils.isEmpty(binderTag)) {
            Log.i(TAG, "Service.onBind, bundle.getString(BinderTag) = null");
            return msgBinder;
        }
        Log.i(TAG, "Service.onBind, bundle.getString(BinderTag) = " + binderTag);
        new Thread(new ExitService()).start();
        return binderManager.getBinderByTag(binderTag);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service.onCreate");
        binderManager = BinderManager.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service.onStartCommand，Default");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service.onDestroy");
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private class ExitService implements Runnable {
        @Override
        public void run() {

            int index = 0;
            while (!mIsServiceDestroyed.get() && index != 15) {
                Log.i(TAG, "--------- timeline = " + index);
                index++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
