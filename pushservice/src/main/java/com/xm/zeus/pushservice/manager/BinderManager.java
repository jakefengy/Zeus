package com.xm.zeus.pushservice.manager;

import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.xm.zeus.chat.utils.ChatClient;
import com.xm.zeus.pushservice.IMsgListener;
import com.xm.zeus.pushservice.IMsgManager;

import java.util.List;

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

}
