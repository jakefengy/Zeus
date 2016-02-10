package com.xm.zeus.chat.entity;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.jivesoftware.smack.packet.Message;

import java.util.Date;

/**
 * 作者：小孩子xm on 2016-02-01 14:38
 * 邮箱：1065885952@qq.com
 */
public class BizMessage {

    private Long messageId;
    private String fromJID;
    private String toJID;
    private java.util.Date sentDate;
    private Message.Type type;
    private String body;

    private BizMessageContent messageContent;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getFromJID() {
        return fromJID;
    }

    public void setFromJID(String fromJID) {
        this.fromJID = fromJID;
    }

    public String getToJID() {
        return toJID;
    }

    public void setToJID(String toJID) {
        this.toJID = toJID;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Message.Type getType() {
        return type;
    }

    public void setType(Message.Type type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BizMessageContent getMessageContent() {
        if (messageContent == null) {
            if (!TextUtils.isEmpty(getBody())) {
                messageContent = JSON.parseObject(getBody(), BizMessageContent.class);
            }
            return messageContent;
        }
        return messageContent;
    }

    public void resetMessageContent() {
        messageContent = null;
    }
}
