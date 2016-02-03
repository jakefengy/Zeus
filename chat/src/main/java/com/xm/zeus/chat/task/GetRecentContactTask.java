package com.xm.zeus.chat.task;

import com.xm.zeus.chat.entity.MAChatListIQ;
import com.xm.zeus.chat.entity.MAChatListItem;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：小孩子xm on 2016-02-01 12:18
 * 邮箱：1065885952@qq.com
 */
public class GetRecentContactTask extends HttpTask {

    private AbstractXMPPConnection xmppConnection;

    public GetRecentContactTask(String strTaskName, AbstractXMPPConnection xmppConnection, GetRecentContactListener listener) {
        super(strTaskName);
        this.xmppConnection = xmppConnection;
        this.listener = listener;
    }

    @Override
    public void doTask() {

        try {
            MAChatListIQ aMAChatListIQ = new MAChatListIQ();

            MAChatListIQ result = xmppConnection.createPacketCollectorAndSend(aMAChatListIQ).nextResultOrThrow();

            final List<MAChatListItem> list = new ArrayList<>();

            if (result != null && result.getItems() != null) {
                list.addAll(result.getItems());
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onSuccess(list);
                    }
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

    private GetRecentContactListener listener;

    public interface GetRecentContactListener {
        void onSuccess(List<MAChatListItem> recentContact);

        void onError(Exception e);
    }

}
