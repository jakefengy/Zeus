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
import android.widget.RemoteViews;

import com.xm.zeus.common.notification.NotificationBarManager;
import com.xm.zeus.service.aidl.IMsgManager;
import com.xm.zeus.service.aidl.IOnReceiveMessageListener;
import com.xm.zeus.service.aidl.MsgEntity;

public class TestRemoteMsgActivity extends AppCompatActivity {

    private final static String TAG = "RemoteMsgTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_msg);

        // init notification
        NotificationBarManager.getInstance().init(getApplication());

        Log.i(TAG, "TestRemoteMsgActivity.onCreate.bindService");
        Intent intent = new Intent(TestRemoteMsgActivity.this, ZeusRemoteService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i(TAG, "binder died. thread name:" + Thread.currentThread().getName());
            if (msgManager == null)
                return;
            msgManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            msgManager = null;
            // TODO:这里重新绑定远程Service
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
        Log.i(TAG, "TestRemoteMsgActivity.handleMsg " + msgEntity.toString());

        switch (msgEntity.getType().getCode()) {
            case "chat":

                final RemoteViews chatRemoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.notify_msg);
                chatRemoteViews.setImageViewResource(R.id.notify_msg_iv_header, R.mipmap.ic_arrow_back);
                chatRemoteViews.setTextViewText(R.id.notify_msg_tv_content, msgEntity.getContent());

                Bundle bundle = new Bundle();
                bundle.putString("Msg", msgEntity.getContent());

                NotificationBarManager.getInstance().showChatNotify(getApplication(), "You have a new Msg", null, 0, ShowMsgActivity.class, bundle, chatRemoteViews);

                break;
            default:
                Bundle bundleOther = new Bundle();
                bundleOther.putString("Msg", msgEntity.getContent());
                NotificationBarManager.getInstance().showOtherNotify(getApplication(), "收到一条新闻", "新闻标题", msgEntity.getContent(), null, 0, ShowMsgActivity.class, bundleOther);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (msgManager != null && msgManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "TestRemoteMsgActivity.onDestroy.unregister listener:" + receiveMsgListener);
                msgManager.unregisterListener(receiveMsgListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
        super.onDestroy();
    }
}
