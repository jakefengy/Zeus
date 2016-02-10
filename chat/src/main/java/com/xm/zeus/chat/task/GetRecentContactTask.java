package com.xm.zeus.chat.task;

import com.xm.zeus.chat.entity.MAChatListIQ;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * 作者：小孩子xm on 2016-02-01 12:18
 * 邮箱：1065885952@qq.com
 */
public class GetRecentContactTask extends HttpTask {

    private AbstractXMPPConnection xmppConnection;

    public GetRecentContactTask(String strTaskName, AbstractXMPPConnection xmppConnection, GetRecentContactTaskListener listener) {
        super(strTaskName);
        this.xmppConnection = xmppConnection;
        this.listener = listener;
    }

    @Override
    public void doTask() {

        try {
            MAChatListIQ aMAChatListIQ = new MAChatListIQ();

            final MAChatListIQ result = xmppConnection.createPacketCollectorAndSend(aMAChatListIQ).nextResultOrThrow();
            if (result == null) {
                onError(new NullPointerException("Recent Contact is null"));
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onSuccess(result);
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }

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

    private GetRecentContactTaskListener listener;

    public interface GetRecentContactTaskListener {
        void onSuccess(MAChatListIQ recentContact);

        void onError(Exception e);
    }

}
