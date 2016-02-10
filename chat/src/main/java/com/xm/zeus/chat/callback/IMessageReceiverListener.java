package com.xm.zeus.chat.callback;

import com.xm.zeus.chat.entity.BizMessage;

/**
 * 作者：小孩子xm on 2016-02-01 13:43
 * 邮箱：1065885952@qq.com
 */
public interface IMessageReceiverListener {

    void newMessage(BizMessage message);

    void receiptMessage(String localId, BizMessage receiptMessage);

    void error(Exception e);

}
