package com.xm.zeus.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.xm.zeus.common.notification.NotificationBarManager;
import com.xm.zeus.common.notification.NotificationType;
import com.xm.zeus.service.aidl.IMsgManager;
import com.xm.zeus.service.aidl.IOnReceiveMessageListener;
import com.xm.zeus.service.aidl.MsgEntity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author fengy on 2016-02-18
 */
public class ZeusRemoteService extends Service {

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<MsgEntity> mMsgList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnReceiveMessageListener> mListenerList = new RemoteCallbackList<>();

    private IBinder msgManager = new IMsgManager.Stub() {

        @Override
        public List<MsgEntity> getMsgList() throws RemoteException {
            return mMsgList;
        }

        @Override
        public void registerListener(IOnReceiveMessageListener listener) throws RemoteException {
            if (listener != null) {
                mListenerList.register(listener);
            }
        }

        @Override
        public void unregisterListener(IOnReceiveMessageListener listener) throws RemoteException {
            if (listener != null) {
                mListenerList.unregister(listener);
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("RemoteMsgTag", "ZeusRemoteService.onBind & send msg ");
        new Thread(new ServiceWorker()).start();
        return msgManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("RemoteMsgTag", "ZeusRemoteService.onCreate");
        // init notification
        NotificationBarManager.getInstance().init(getApplication());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("RemoteMsgTag", "ZeusRemoteService.onStartCommand");
        return START_NOT_STICKY;
    }

    private void onNewMsgArrived(MsgEntity msg) throws RemoteException {
        mMsgList.add(msg);
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnReceiveMessageListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onReceiveMessage(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
        handleMsg(msg);
    }

    private void handleMsg(MsgEntity msgEntity) {
        Log.i("RemoteMsgTag", "ZeusRemoteService.handleMsg " + msgEntity.toString());

        switch (msgEntity.getType().getCode()) {
            case "chat":

                final RemoteViews chatRemoteViews = new RemoteViews(getApplication().getPackageName(), R.layout.notify_msg);
                chatRemoteViews.setImageViewResource(R.id.notify_msg_iv_header, R.mipmap.ic_arrow_back);
                chatRemoteViews.setTextViewText(R.id.notify_msg_tv_content, msgEntity.getContent());

                Bundle bundle = new Bundle();
                bundle.putString("Msg", msgEntity.getContent());

                NotificationBarManager.getInstance().showChatNotify(getApplication(), "You have a new Msg", null, ShowMsgActivity.class, bundle, chatRemoteViews);

                break;
            default:
                Bundle bundleOther = new Bundle();
                bundleOther.putString("Msg", msgEntity.getContent());
                NotificationBarManager.getInstance().showOtherNotify(getApplication(), "收到一条新闻", "新闻标题", msgEntity.getContent(), null, ShowMsgActivity.class, bundleOther);

                break;
        }
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestroyed.get() && mMsgList.size() < 5) {

                int index = mMsgList.size() + 1;

                try {
                    if (mMsgList.size() == 3) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("RemoteMsgTag", "ZeusRemoteService.Process.killProcess(" + Process.myPid() + ")");
//                                Process.killProcess(Process.myPid());
                                throw new NullPointerException();
                            }
                        });
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                MsgEntity newMsg;
                if (index - 1 < 4) {
                    newMsg = new MsgEntity(NotificationType.CHAT, "消息 from " + index);
                } else if (index - 1 >= 4 && index - 1 < 8) {
                    newMsg = new MsgEntity(NotificationType.OTHER, "新闻 from" + index);
                } else {
                    newMsg = new MsgEntity(NotificationType.OTHER, "Activity is Destroy from" + index);
                }

                try {
                    onNewMsgArrived(newMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i("RemoteMsgTag", "ZeusRemoteService.onDestroy");
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }
}
