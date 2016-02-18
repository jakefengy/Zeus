package com.xm.zeus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

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

        return msgManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new ServiceWorker()).start();

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
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestroyed.get() && mMsgList.size() < 10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = mMsgList.size() + 1;
                MsgEntity newMsg;
                if (index - 1 < 5) {
                    newMsg = new MsgEntity(NotificationType.CHAT, "消息 from " + index);
                } else {
                    newMsg = new MsgEntity(NotificationType.OTHER, "新闻 from" + index);
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
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }
}
