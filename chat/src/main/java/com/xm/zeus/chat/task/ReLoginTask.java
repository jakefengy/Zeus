package com.xm.zeus.chat.task;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * 作者：小孩子xm on 2016-02-01 12:26
 * 邮箱：1065885952@qq.com
 */
public class ReLoginTask extends HttpTask {

    private AbstractXMPPConnection xmppConnection;

    public ReLoginTask(String strTaskName, AbstractXMPPConnection xmppConnection, ReLoginListener listener) {
        super(strTaskName);
        this.xmppConnection = xmppConnection;
        this.listener = listener;
    }

    @Override
    public void doTask() {

        try {
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
                if (listener != null) {
                    listener.onSuccess();
                }
            }
        });
    }

    @Override
    public void onError(final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    private ReLoginListener listener;

    public interface ReLoginListener {
        void onSuccess();

        void onError(Exception e);
    }
}
