package com.xm.zeus.pushservice.manager;

import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;

import com.xm.zeus.chat.utils.ChatClient;
import com.xm.zeus.common.aidl.IMsgListener;
import com.xm.zeus.common.aidl.IMsgManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author fengy on 2016-02-23
 */
public class BinderManager {

    private static BinderManager instance;

    private BinderManager() {
        chatClient = ChatClient.getInstance();
    }

    public static synchronized BinderManager getInstance() {
        if (instance == null) {
            instance = new BinderManager();
        }
        return instance;
    }

    //
    private ChatClient chatClient;

    public IBinder getBinderByTag(String tag) {

        if (TextUtils.isEmpty(tag)) {
            return null;
        }

        if (tag.equals(BinderTag.MESSAGE.getName())) {
            return msgBinder;
        }

        return null;

    }

    public enum BinderTag {

        LOGIN {
            @Override
            public String getName() {
                return "login";
            }
        },

        MESSAGE {
            @Override
            public String getName() {
                return "msg";
            }
        };

        public abstract String getName();
    }

    // About Message
    private CopyOnWriteArrayList<String> mMsgList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IMsgListener> mListenerList = new RemoteCallbackList<>();

    private IBinder msgBinder = new IMsgManager.Stub() {
        @Override
        public List<String> getMsgs() throws RemoteException {
            return mMsgList;
        }

        @Override
        public void registerListener(IMsgListener listener) throws RemoteException {
            if (listener != null) {
                mListenerList.register(listener);
            }
        }

        @Override
        public void unregisterListener(IMsgListener listener) throws RemoteException {
            if (listener != null) {
                mListenerList.unregister(listener);
            }
        }
    };

    public void notifyNewMsg(String message) {
        mMsgList.add(message);

        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IMsgListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewMsg(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();

    }

}
