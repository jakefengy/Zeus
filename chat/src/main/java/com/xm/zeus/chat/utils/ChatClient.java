package com.xm.zeus.chat.utils;

import android.text.TextUtils;

import com.xm.zeus.chat.callback.ILoginOutListener;
import com.xm.zeus.chat.task.ClearUnreadMsgNumTask;
import com.xm.zeus.chat.task.GetChatHistoryTask;
import com.xm.zeus.chat.task.GetRecentContactTask;
import com.xm.zeus.chat.task.LoginTask;
import com.xm.zeus.chat.task.MessageReceiver;
import com.xm.zeus.chat.task.ReLoginTask;
import com.xm.zeus.chat.task.SendMsgTask;
import com.xm.zeus.chat.task.TaskManager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.XMPPConnection;

import java.util.Date;

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

    private String serviceName;
    private String serviceHost;
    private int servicePort;

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

    // ---------------------聊天相关请求---------------------------- //


    /**
     * 检查xmppConnection是否可用
     *
     * @return true isAvailable false unAvailable
     */
    private boolean isConnectionAvailable() {
        return xmppConnection != null && xmppConnection.isConnected() && xmppConnection.isAuthenticated();
    }

    // 登录
    public void login(String serviceName, String serviceHost, int servicePort, String userName, String password, String source, final LoginTask.LoginTaskListener loginListener) {

        if (TextUtils.isEmpty(serviceName)) {
            if (loginListener != null) {
                loginListener.onError(new NullPointerException("serviceName is empty"));
            }
            return;
        }

        if (TextUtils.isEmpty(serviceHost)) {
            if (loginListener != null) {
                loginListener.onError(new NullPointerException("serviceHost is empty"));
            }
            return;
        }

        if (TextUtils.isEmpty(userName)) {
            if (loginListener != null) {
                loginListener.onError(new NullPointerException("userName is empty"));
            }
            return;
        }

        if (TextUtils.isEmpty(password)) {
            if (loginListener != null) {
                loginListener.onError(new NullPointerException("password is empty"));
            }
            return;
        }

        if (TextUtils.isEmpty(source)) {
            if (loginListener != null) {
                loginListener.onError(new NullPointerException("source is empty"));
            }
            return;
        }

        setServiceHost(serviceHost);
        setServiceName(serviceName);
        setServicePort(servicePort);

        LoginTask loginTask = new LoginTask("Login", serviceName, serviceHost, servicePort, userName, password, source, new LoginTask.LoginTaskListener() {
            @Override
            public void onSuccess(AbstractXMPPConnection connection) {

                loginSuccessful();

                if (loginListener != null) {
                    loginListener.onSuccess(connection);
                }
            }

            @Override
            public void onError(Exception e) {
                if (loginListener != null) {
                    loginListener.onError(e);
                }
            }
        });

        mTaskMgr.addTask(loginTask);
    }

    // 登录成功之后的初始化
    public void loginSuccessful() {

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
            if (!isConnectionAvailable()) {
                if (listener != null) {
                    listener.onLoginOutFailed(new Exception("connection is null or no longer connected"));
                }
                return;
            }

            beforeLoginOut();

            xmppConnection.disconnect();
            xmppConnection = null;

            if (listener != null) {
                listener.onLoginOutSuccessful();
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
    public void reLogin(ReLoginTask.ReLoginListener listener) {

        if (!isConnectionAvailable()) {
            if (listener != null) {
                listener.onError(new Exception("connection is null or no longer connected"));
            }
            return;
        }

        ReLoginTask reLoginTask = new ReLoginTask("ReLoginTask", xmppConnection, listener);

        mTaskMgr.addTask(reLoginTask);
    }

    // 获取最近联系人列表
    public void getRecentContact(GetRecentContactTask.GetRecentContactListener listener) {

        if (!isConnectionAvailable()) {
            if (listener != null) {
                listener.onError(new Exception("connection is null or no longer connected"));
            }
            return;
        }

        GetRecentContactTask task = new GetRecentContactTask("GetRecentContactTask", xmppConnection, listener);

        mTaskMgr.addTask(task);
    }

    // 获取消息历史记录
    public void getChatHistory(String userId, Date msgDate, int pageSize, GetChatHistoryTask.GetChatHistoryListener listener) {

        if (!isConnectionAvailable()) {
            if (listener != null) {
                listener.onError(new NullPointerException("connection is null or no longer connected"));
            }
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            if (listener != null) {
                listener.onError(new NullPointerException("userId is empty"));
            }
            return;
        }

        if (msgDate == null) {
            if (listener != null) {
                listener.onError(new NullPointerException("msgDate is empty"));
            }
            return;
        }

        if (pageSize <= 0) {
            if (listener != null) {
                listener.onError(new Exception("pageSize mast ge 0, now pageSize = " + pageSize));
            }
            return;
        }

        GetChatHistoryTask getChatHistory = new GetChatHistoryTask("GetChatHistoryTask", xmppConnection, userId, getServiceHost(), msgDate, pageSize, listener);

        mTaskMgr.addTask(getChatHistory);
    }

    // 清除未读消息数
    public void clearNumberOfUnreadMessages(String userId, ClearUnreadMsgNumTask.ClearUnreadMsgNumListener listener) {

        if (!isConnectionAvailable()) {
            if (listener != null) {
                listener.onError(new NullPointerException("connection is null or no longer connected"));
            }
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            if (listener != null) {
                listener.onError(new NullPointerException("userId is empty"));
            }
            return;
        }

        if (TextUtils.isEmpty(getServiceHost())) {
            if (listener != null) {
                listener.onError(new NullPointerException("ServiceHost is empty"));
            }
            return;
        }

        ClearUnreadMsgNumTask clearUnreadMsgNum = new ClearUnreadMsgNumTask("ClearUnreadMsgNumTask", xmppConnection, userId, getServiceHost(), listener);

        mTaskMgr.addTask(clearUnreadMsgNum);
    }

    // 发送消息
    public void sendMessage(String localId, String withId, String msg, SendMsgTask.SendMsgListener listener) {

        if (!isConnectionAvailable()) {
            if (listener != null) {
                listener.onError(new NullPointerException("connection is null or no longer connected"));
            }
            return;
        }

        if (TextUtils.isEmpty(withId)) {
            if (listener != null) {
                listener.onError(new NullPointerException("withId is empty"));
            }
            return;
        }

        if (TextUtils.isEmpty(getServiceHost())) {
            if (listener != null) {
                listener.onError(new NullPointerException("ServiceHost is empty"));
            }
            return;
        }

        if (TextUtils.isEmpty(msg)) {
            if (listener != null) {
                listener.onError(new NullPointerException("msg is empty"));
            }
            return;
        }

        SendMsgTask sendMsgTask = new SendMsgTask("SendMsgTask", xmppConnection, getServiceHost(), localId, withId, msg, listener);

        mTaskMgr.addTask(sendMsgTask);
    }

    // ---------------------get set---------------------------- //

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

}
