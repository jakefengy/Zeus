package com.xm.zeus.chat.task;

import com.xm.zeus.chat.callback.IMessageReceiverListener;
import com.xm.zeus.chat.entity.BizMessage;
import com.xm.zeus.chat.entity.MAChatListIQ;
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

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.ProviderManager;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 作者：小孩子xm on 2016-02-01 13:40
 * 邮箱：1065885952@qq.com
 */
public class MessageReceiver {

    private static MessageReceiver instance;

    private ChatManagerListener chatManagerListener;

    private Set<IMessageReceiverListener> messageReceiverListeners = new CopyOnWriteArraySet<>();

    private MessageReceiver() {
        chatManagerListener = new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        handleMessage(message);

                    }
                });
            }
        };

    }

    public static MessageReceiver getInstance() {
        if (instance == null) {
            synchronized (MessageReceiver.class) {
                if (instance == null) {
                    instance = new MessageReceiver();
                }
            }
        }
        return instance;
    }

    // 添加新消息监听
    public void addChatManagerListener(AbstractXMPPConnection connection) {
        if (connection != null) {

            // 注册消息解析器
            ProviderManager.addIQProvider(MUCRoomListIQ.ELEMENT, MUCRoomListIQ.NAMESPACE, new MUCRoomListProvider());
            ProviderManager.addIQProvider(MAChatListIQ.ELEMENT, MAChatListIQ.NAMESPACE, new MAChatListProvider());
            ProviderManager.addIQProvider(MAMessageListIQ.ELEMENT, MAMessageListIQ.NAMESPACE, new MAMessageListProvider());
            ProviderManager.addIQProvider(MAChatModifyIQ.ELEMENT, MAChatModifyIQ.NAMESPACE, new MAChatModifyProvider());
            ProviderManager.addExtensionProvider(MAMSGExtension.ELEMENT_MSGEXT, MAMSGExtension.NAMESPACE, new MAMSGExtensionProvider());
            ProviderManager.addExtensionProvider(MALocalIdExtension.ELEMENT_EXT_LOCALID, MALocalIdExtension.NAMESPACE, new MALocalIdExtensionProvider());

            ChatManager.getInstanceFor(connection).addChatListener(chatManagerListener);
        }
    }

    // 移除新消息监听
    public void removeChatManagerListener(AbstractXMPPConnection connection) {
        if (connection != null) {
            // 移除消息解析器
            ProviderManager.removeIQProvider(MUCRoomListIQ.ELEMENT, MUCRoomListIQ.NAMESPACE);
            ProviderManager.removeIQProvider(MAChatListIQ.ELEMENT, MAChatListIQ.NAMESPACE);
            ProviderManager.removeIQProvider(MAMessageListIQ.ELEMENT, MAMessageListIQ.NAMESPACE);
            ProviderManager.removeIQProvider(MAChatModifyIQ.ELEMENT, MAChatModifyIQ.NAMESPACE);
            ProviderManager.removeExtensionProvider(MAMSGExtension.ELEMENT_MSGEXT, MAMSGExtension.NAMESPACE);
            ProviderManager.removeExtensionProvider(MALocalIdExtension.ELEMENT_EXT_LOCALID, MALocalIdExtension.NAMESPACE);

            ChatManager.getInstanceFor(connection).removeChatListener(chatManagerListener);
        }
    }

    // 消息分发
    private void handleMessage(Message message) {

        if (message == null) {
            error("message is null");
            return;
        }

        if (message.getType() == Message.Type.chat || message.getType() == Message.Type.normal) {

            MAMSGExtension mamsgExtension = message.getExtension(MAMSGExtension.ELEMENT_MSGEXT, MAMSGExtension.NAMESPACE);

            if (mamsgExtension != null) {

            }
        }

    }

    // 接收到新消息
    private void receiveNewMsg(BizMessage message) {
        for (IMessageReceiverListener listener : messageReceiverListeners) {
            listener.newMessage(message);
        }
    }

    // 接收到回执消息
    private void receiptMessage(String localId, BizMessage receiptMessage) {
        for (IMessageReceiverListener listener : messageReceiverListeners) {
            listener.receiptMessage(localId, receiptMessage);
        }
    }

    // 消息处理过程中发生错误
    private void error(String message) {
        Exception exception = new Exception(message);
        for (IMessageReceiverListener listener : messageReceiverListeners) {
            listener.error(exception);
        }
    }

}
