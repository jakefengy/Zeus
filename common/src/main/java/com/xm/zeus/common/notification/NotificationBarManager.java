package com.xm.zeus.common.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import java.util.HashMap;
import java.util.List;

/**
 * @author fengy on 2016-02-17
 */
public class NotificationBarManager {

    private static NotificationBarManager instance;

    private NotificationBarManager() {

    }

    public synchronized static NotificationBarManager getInstance() {
        if (instance == null) {
            instance = new NotificationBarManager();
        }
        return instance;
    }

    // Notification管理
    public NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private HashMap<NotificationType, List<Integer>> notificationTypeIdMap;

    public void init(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationTypeIdMap = new HashMap<>();

        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT);

    }

    public void showChatNotify(Context context, int notifyId, String ticker, String contentTitle, String contentText, Intent intent, Bundle bundle, int flags) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, flags);

        mBuilder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setTicker(ticker);



        mNotificationManager.notify(notifyId, mBuilder.build());
    }

}
