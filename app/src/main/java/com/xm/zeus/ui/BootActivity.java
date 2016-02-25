package com.xm.zeus.ui;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xm.zeus.R;
import com.xm.zeus.common.Logger;
import com.xm.zeus.common.aidl.IMsgListener;
import com.xm.zeus.common.aidl.IMsgManager;

public class BootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boot);

        Intent intent = new Intent();
        intent.setAction("com.xm.zeus.action.ZeusRemoteService");
        intent.putExtra("BinderTag", "msg");
        print("BootActivity.bindService");
        BootActivity.this.bindService(intent, conn, Service.BIND_AUTO_CREATE);

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            print("BootActivity.onServiceConnected");
            msgManager = IMsgManager.Stub.asInterface(service);
            try {
                msgManager.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            print("BootActivity.onServiceDisconnected");
            msgManager = null;
        }
    };

    // Manager
    private IMsgManager msgManager = null;

    private IMsgListener listener = new IMsgListener.Stub() {
        @Override
        public void onNewMsg(String msg) throws RemoteException {
            print(msg);
        }
    };

    @Override
    protected void onDestroy() {

        if (msgManager != null && msgManager.asBinder().isBinderAlive()) {
            print("BootActivity.onDestroy unregisterListener & unbindService");
            try {
                msgManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(conn);

        super.onDestroy();
    }

    protected void print(String content) {
        Log.i(TAG, content);
        try {
            Logger.writeLog(TAG + " : " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static final String TAG = "ClientTag";

}
