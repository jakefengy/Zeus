package com.xm.zeus.common.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

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

    // 声音控制
    private AudioManager am = null;
    private int currentSound;

    public void init(Context appContext) {
        mNotificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationTypeIdMap = new HashMap<>();
        notifyId = 0;

        am = (AudioManager) appContext.getSystemService(appContext.AUDIO_SERVICE);
        currentSound = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    public boolean showChatNotify(Context appContext, String ticker, String contentTitle, String contentText, Intent intent, RemoteViews remoteviews) {

        if (TextUtils.isEmpty(ticker) || TextUtils.isEmpty(contentTitle) || TextUtils.isEmpty(contentText)) {
            return false;
        }

        if (remoteviews == null) {
            return false;
        }

        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(appContext);
        }

        mBuilder.setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setTicker(ticker);

        PendingIntent pendingIntent = null;

        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(appContext, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        if (pendingIntent != null) {
            mBuilder.setContentIntent(pendingIntent);
        }

        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.contentView = remoteviews;

        mNotificationManager.notify(notifyId, notification);

        recordNotifyId(NotificationType.CHAT, notifyId);

        notifyId++;

        wakeUp(appContext);

        return true;

    }

    private void recordNotifyId(NotificationType notificationType, int notifyId) {
        if (notificationTypeIdMap == null) {
            notificationTypeIdMap = new HashMap<>();
        }

        List<Integer> ids;
        if (notificationTypeIdMap.containsKey(notificationType)) {
            ids = notificationTypeIdMap.get(notificationType);
        } else {
            ids = new ArrayList<>();
        }

        ids.add(notifyId);
        notificationTypeIdMap.put(notificationType, ids);

    }

    public void cancelById(int notifyId) {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(notifyId);
        }
    }

    public void cancelByType(NotificationType type) {
        if (notificationTypeIdMap == null) {
            notificationTypeIdMap = new HashMap<>();
        }

        if (notificationTypeIdMap.containsKey(type)) {
            for (int id : notificationTypeIdMap.get(type)) {
                cancelById(id);
            }

            notificationTypeIdMap.remove(type);
        }

    }

    // Wake up the screen
    private void wakeUp(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        wl.acquire();
        wl.release();

    }

    // play audio
    public void playAudio(Context appContext, int audioResId) {

        MediaPlayer mPlayer = MediaPlayer.create(appContext, audioResId);
        mPlayer.setLooping(false);
        mPlayer.setVolume(currentSound, currentSound);

        mPlayer.start();

    }

    // shock 0.2s
    public void playShock(Context appContext) {

        Vibrator vib = (Vibrator) appContext.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(200);

    }
}
