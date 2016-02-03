package com.xm.zeus.chat.task;

import com.xm.zeus.chat.entity.MALocalIdExtension;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;

/**
 * @author fengy on 2016-02-03
 */
public class SendMsgTask extends HttpTask {


    private AbstractXMPPConnection xmppConnection;

    private String localId;
    private String withId;
    private String serviceHost;
    private String msg;

    private SendMsgListener listener;


    public SendMsgTask(String strTaskName, AbstractXMPPConnection xmppConnection, String serviceHost, String localId, String withId, String msg, SendMsgListener listener) {
        super(strTaskName);
        this.xmppConnection = xmppConnection;
        this.localId = localId;
        this.withId = withId;
        this.serviceHost = serviceHost;
        this.msg = msg;
        this.listener = listener;
    }

    @Override
    public void doTask() {
        try {

            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(msg);

            MALocalIdExtension localIdExtension = new MALocalIdExtension();
            localIdExtension.setLocalid(localId);

            message.addExtension(localIdExtension);

            ChatManager chatManager = ChatManager.getInstanceFor(xmppConnection);

            EntityJid jid = (EntityJid) JidCreate.from(withId + "@" + serviceHost);

            Chat chat = chatManager.createChat(jid);

            chat.sendMessage(message);

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

    public interface SendMsgListener {
        void onSuccess();

        void onError(Exception e);
    }


}
