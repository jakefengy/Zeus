package com.xm.zeus.common.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;
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
    private int notifyId; // 自增长

    public void init(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationTypeIdMap = new HashMap<>();
        notifyId = 0;

        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT);

    }

    public void showChatNotify(Context context, String ticker, String contentTitle, String contentText, Intent intent, Bundle bundle, int flags) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, flags);

        mBuilder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setTicker(ticker);


        mNotificationManager.notify(notifyId, mBuilder.build());
        recordNotifyId(NotificationType.CHAT, notifyId);
        notifyId++;

    }

    private void recordNotifyId(NotificationType notificationType, int notifyId) {
        if (notificationTypeIdMap == null) {
            notificationTypeIdMap = new HashMap<>();
        }

        List<Integer> ids = null;
        if (notificationTypeIdMap.containsKey(notificationType)) {
            ids = notificationTypeIdMap.get(notificationType);
        } else {
            ids = new ArrayList<>();
        }

        ids.add(notifyId);
        notificationTypeIdMap.put(notificationType, ids);

    }

    public void cancelNotifyById(int notifyId) {

    }

    public void cancelNotifyByType(NotificationType type) {

    }

}
