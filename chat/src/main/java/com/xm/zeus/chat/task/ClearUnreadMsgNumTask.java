package com.xm.zeus.chat.task;

import com.xm.zeus.chat.entity.MAChatModifyIQ;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ExceptionCallback;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;

/**
 * @author fengy on 2016-02-03
 */
public class ClearUnreadMsgNumTask extends HttpTask {


    private AbstractXMPPConnection xmppConnection;

    private String userId;
    private String serviceHost;

    private ClearUnreadMsgNumListener listener;


    public ClearUnreadMsgNumTask(String strTaskName, AbstractXMPPConnection xmppConnection, String userId, String serviceHost, ClearUnreadMsgNumListener listener) {
        super(strTaskName);
        this.xmppConnection = xmppConnection;
        this.userId = userId;
        this.serviceHost = serviceHost;
        this.listener = listener;
    }

    @Override
    public void doTask() {
        try {

            MAChatModifyIQ aMAChatModifyIQ = new MAChatModifyIQ(userId + "@" + serviceHost, MAChatModifyIQ.Action.unreadcount);
            xmppConnection.sendIqWithResponseCallback(aMAChatModifyIQ, new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        }
                    });
                }
            }, new ExceptionCallback() {
                @Override
                public void processException(Exception e) {
                    onError(e);
                }
            });

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

    public interface ClearUnreadMsgNumListener {
        void onSuccess();

        void onError(Exception e);
    }


}
