package com.xm.zeus.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xm.zeus.service.aidl.IMsgManager;
import com.xm.zeus.service.aidl.IOnReceiveMessageListener;
import com.xm.zeus.service.aidl.MsgEntity;

public class TestRemoteMsgActivity extends AppCompatActivity {

    private final static String TAG = "RemoteMsgTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_msg);

        Log.i(TAG, "TestRemoteMsgActivity.onCreate.bindService");
        Intent intent = new Intent(TestRemoteMsgActivity.this, ZeusRemoteService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i(TAG, "--------------------------------------------binder died. thread name:" + Thread.currentThread().getName());
            if (msgManager == null)
                return;
            msgManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            msgManager = null;
        }
    };

    private IMsgManager msgManager = null;

    private IOnReceiveMessageListener receiveMsgListener = new IOnReceiveMessageListener.Stub() {
        @Override
        public void onReceiveMessage(MsgEntity msg) throws RemoteException {
            handler.obtainMessage(0, msg).sendToTarget();
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "TestRemoteMsgActivity.onServiceConnected");
            IMsgManager iMsgManager = IMsgManager.Stub.asInterface(service);
            msgManager = iMsgManager;

            try {
                msgManager.asBinder().linkToDeath(mDeathRecipient, 0);
                msgManager.registerListener(receiveMsgListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "TestRemoteMsgActivity.onServiceDisconnected");
            msgManager = null;
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    handleMsg((MsgEntity) msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private void handleMsg(MsgEntity msgEntity) {
//        Log.i(TAG, "TestRemoteMsgActivity.handleMsg " + msgEntity.toString());
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    private void release() {
        if (msgManager != null && msgManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "TestRemoteMsgActivity.onDestroy.unregister listener:" + receiveMsgListener);
                msgManager.unregisterListener(receiveMsgListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
    }

}
