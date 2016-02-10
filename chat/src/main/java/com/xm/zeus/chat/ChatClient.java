package com.xm.zeus.chat;

import com.xm.zeus.chat.callback.ILoginListener;
import com.xm.zeus.chat.callback.ILoginOutListener;
import com.xm.zeus.chat.callback.IReLoginListener;
import com.xm.zeus.chat.callback.IRecentContactListener;
import com.xm.zeus.chat.entity.MAChatListIQ;
import com.xm.zeus.chat.entity.MAChatListItem;
import com.xm.zeus.chat.entity.MAChatListProvider;
import com.xm.zeus.chat.entity.MAChatModifyIQ;
import com.xm.zeus.chat.entity.MAChatModifyProvider;
import com.xm.zeus.chat.entity.MALocalIdExtension;
import com.xm.zeus.chat.entity.MALocalIdExtensionProvider;
import com.xm.zeus.chat.entity.MAMSGExtension;
import com.xm.zeus.chat.entity.MAMSGExtensionProvider;
import com.xm.zeus.chat.entity.MAMessageListIQ;
import com.xm.zeus.chat.entity.MAMessageListProvider;
import com.xm.zeus.chat.entity.MUCRoomListIQ;
import com.xm.zeus.chat.entity.MUCRoomListProvider;
import com.xm.zeus.chat.task.GetRecentContactTask;
import com.xm.zeus.chat.task.LoginTask;
import com.xm.zeus.chat.task.MessageReceiver;
import com.xm.zeus.chat.task.ReLoginTask;
import com.xm.zeus.chat.task.TaskManager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;

import java.util.ArrayList;

/**
 * 聊天管理类
 * <p>
 * 作者：小孩子xm on 2016-01-31 13:38
 * 邮箱：1065885952@qq.com
 */
public class ChatClient {


    private static ChatClient instance;
    private final static Object syncLock = new Object();

    // 任务管理器
    private TaskManager mTaskMgr;

    private AbstractXMPPConnection xmppConnection;
    private ReconnectionManager reconnectionManager;

    private ChatClient() {
        mTaskMgr = new TaskManager();
        mTaskMgr.init(0);
    }

    public static ChatClient getInstance() {

        if (instance == null) {
            synchronized (syncLock) {
                if (instance == null) {
                    instance = new ChatClient();
                }
            }
        }

        return instance;
    }

    // 检查连接是否可用
    private boolean isConnectionAvailable() {
        return xmppConnection != null && xmppConnection.isConnected() && xmppConnection.isAuthenticated();
    }

    // 登录
    public void login(String serviceName, String serviceHost, int servicePort, String userName, String password, final ILoginListener loginListener) {
        LoginTask loginTask = new LoginTask("Login", serviceName, serviceHost, servicePort, userName, password, new LoginTask.LoginTaskListener() {
            @Override
            public void onSuccess(AbstractXMPPConnection connection) {

                xmppConnection = connection;

                loginSuccessful();

                if (loginListener != null) {
                    loginListener.onLoginSuccessful();
                }
            }

            @Override
            public void onError(Exception e) {
                if (loginListener != null) {
                    loginListener.onLoginFailed(e);
                }
            }
        });

        mTaskMgr.addTask(loginTask);
    }

    private void loginSuccessful() {

        // 启动重连机制
        reconnectionManager = ReconnectionManager.getInstanceFor(xmppConnection);
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.RANDOM_INCREASING_DELAY);
        reconnectionManager.enableAutomaticReconnection();

        // 连接状态监听
        xmppConnection.addConnectionListener(connectionListener);

        // 添加消息接收器
        MessageReceiver.getInstance().addChatManagerListener(xmppConnection);

    }

    // 登出
    public void loginOut(ILoginOutListener listener) {
        try {
            if (isConnectionAvailable()) {

                beforeLoginOut();

                xmppConnection.disconnect();
                xmppConnection = null;

                if (listener != null) {
                    listener.onLoginOutSuccessful();
                }
            } else {
                if (listener != null) {
                    listener.onLoginOutFailed(new Exception("connection is null or no longer connected"));
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onLoginOutFailed(e);
            }
        }
    }

    private void beforeLoginOut() {

        MessageReceiver.getInstance().removeChatManagerListener(xmppConnection);

        // 取消重连机制
        if (reconnectionManager != null) {
            reconnectionManager.disableAutomaticReconnection();
        }

        // 取消连接状态监听
        xmppConnection.removeConnectionListener(connectionListener);
    }

    // 连接状态监听
    private ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {

        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {

        }

        @Override
        public void connectionClosed() {

        }

        @Override
        public void connectionClosedOnError(Exception e) {

        }

        @Override
        public void reconnectionSuccessful() {

        }

        @Override
        public void reconnectingIn(int seconds) {

        }

        @Override
        public void reconnectionFailed(Exception e) {

        }
    };

    // 重新登录
    public void reLogin(final IReLoginListener listener) {

        if (isConnectionAvailable()) {

            ReLoginTask reLoginTask = new ReLoginTask("ReLoginTask", xmppConnection, new ReLoginTask.ReLoginTaskListener() {
                @Override
                public void onSuccess() {
                    if (listener != null) {
                        listener.onReLoginSuccessful();
                    }
                }

                @Override
                public void onError(Exception e) {
                    if (listener != null) {
                        listener.onReLoginFailed(e);
                    }
                }
            });

            mTaskMgr.addTask(reLoginTask);
        } else {
            if (listener != null) {
                listener.onReLoginFailed(new Exception("connection is null or no longer connected"));
            }
        }
    }

    // 获取最近联系人列表
    public void getRecentContact(final IRecentContactListener listener) {

        if (isConnectionAvailable()) {

            GetRecentContactTask task = new GetRecentContactTask("GetRecentContactTask", xmppConnection, new GetRecentContactTask.GetRecentContactTaskListener() {
                @Override
                public void onSuccess(MAChatListIQ recentContact) {
                    if (recentContact.getItems() != null) {
                        if (listener != null) {
                            listener.onSuccessful(recentContact.getItems());
                        }
                    } else {
                        if (listener != null) {
                            listener.onSuccessful(new ArrayList<MAChatListItem>());
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            });

            mTaskMgr.addTask(task);
        } else {
            if (listener != null) {
                listener.onError(new Exception("connection is null or no longer connected"));
            }
        }
    }


}
