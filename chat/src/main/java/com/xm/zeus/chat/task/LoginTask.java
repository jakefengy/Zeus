package com.xm.zeus.chat.task;

import com.xm.zeus.chat.BuildConfig;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;

/**
 * 登录聊天服务器
 * <p/>
 * 作者：小孩子xm on 2016-01-31 13:50
 * 邮箱：1065885952@qq.com
 */
public class LoginTask extends HttpTask {

    public interface LoginTaskListener {

        void onSuccess(AbstractXMPPConnection connection);

        void onError(Exception e);

    }

    private LoginTaskListener taskListener;

    private String serviceName;
    private String serviceHost;
    private int servicePort;

    private String userName;
    private String password;
    private String resource;

    private AbstractXMPPConnection xmppConnection;

    public LoginTask(String strTaskName, String serviceName, String serviceHost, int servicePort, String userName, String password, String resource, LoginTaskListener taskListener) {
        super(strTaskName);
        this.taskListener = taskListener;
        this.serviceName = serviceName;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.userName = userName;
        this.password = password;
        this.resource = resource;
    }

    @Override
    public void doTask() {
        try {

            DomainBareJid jid = (DomainBareJid) JidCreate.from(serviceName);

            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(userName + "@" + serviceHost, password)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setXmppDomain(jid)
                    .setHost(serviceHost)
                    .setPort(servicePort)
                    .setResource(resource)
                    .setSendPresence(true)
                    .setDebuggerEnabled(BuildConfig.DEBUG);

            xmppConnection = new XMPPTCPConnection(config.build());
            xmppConnection.connect();
            xmppConnection.login();

            onSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }

    @Override
    public void onSuccess() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (taskListener != null) {
                    taskListener.onSuccess(xmppConnection);
                }
            }
        });
    }

    @Override
    public void onError(final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (taskListener != null) {
                    taskListener.onError(e);
                }
            }
        });
    }

}
