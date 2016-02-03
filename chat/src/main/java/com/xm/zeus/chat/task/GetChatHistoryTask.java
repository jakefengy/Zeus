package com.xm.zeus.chat.task;

import com.xm.zeus.chat.entity.MAMessageListIQ;
import com.xm.zeus.chat.entity.MAMessageListItem;

import org.jivesoftware.smack.AbstractXMPPConnection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fengy on 2016-02-03
 */
public class GetChatHistoryTask extends HttpTask {

    private AbstractXMPPConnection xmppConnection;

    private String userId;
    private String serviceHost;
    private Date msgDate;
    private int pageSize;

    private GetChatHistoryListener listener;

    public GetChatHistoryTask(String strTaskName, AbstractXMPPConnection xmppConnection, String userId, String serviceHost, Date msgDate, int pageSize, GetChatHistoryListener listener) {
        super(strTaskName);
        this.xmppConnection = xmppConnection;
        this.userId = userId;
        this.serviceHost = serviceHost;
        this.msgDate = msgDate;
        this.pageSize = pageSize;
        this.listener = listener;
    }

    @Override
    public void doTask() {

        try {

            MAMessageListIQ aMAMessageListIQ = new MAMessageListIQ(userId + "@" + serviceHost);
            aMAMessageListIQ.setEndDate(msgDate);
            aMAMessageListIQ.setPageSize(pageSize);
            MAMessageListIQ result = (MAMessageListIQ) (xmppConnection.createPacketCollectorAndSend(aMAMessageListIQ).nextResultOrThrow());

            final List<MAMessageListItem> records = new ArrayList<>();

            if (result != null && result.getItems() != null) {
                records.addAll(result.getItems());
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onSuccess(records);
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

    public interface GetChatHistoryListener {
        void onSuccess(List<MAMessageListItem> records);

        void onError(Exception e);
    }

}
