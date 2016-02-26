package com.xm.zeus.views;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.xm.zeus.R;
import com.xm.zeus.base.ServiceBaseActivity;
import com.xm.zeus.common.aidl.IMsgListener;
import com.xm.zeus.common.aidl.IMsgManager;

public class BootActivity extends ServiceBaseActivity {

    // Manager
    private IMsgManager msgManager = null;
    private IMsgListener listener;

    @Override
    protected void initVariables() {
        listener = new IMsgListener.Stub() {
            @Override
            public void onNewMsg(String msg) throws RemoteException {
                print(msg);
            }
        };
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_boot);
    }

    @Override
    protected void onConnectedService(ComponentName name, IBinder service) {
        print("BootActivity.onServiceConnected");
        msgManager = IMsgManager.Stub.asInterface(service);
        try {
            msgManager.registerListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Intent setServiceIntent() {
        Intent intent = new Intent();
        intent.setAction("com.xm.zeus.action.ZeusRemoteService");
        intent.putExtra("BinderTag", "msg");
        print("BootActivity.bindService");
        return intent;
    }

    @Override
    protected void onDisconnectedService(ComponentName name) {
        print("BootActivity.onServiceDisconnected");
        msgManager = null;
    }

    @Override
    protected void onReleaseService() {
        if (msgManager != null && msgManager.asBinder().isBinderAlive()) {
            print("BootActivity.onDestroy unregisterListener & unbindService");
            try {
                msgManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
