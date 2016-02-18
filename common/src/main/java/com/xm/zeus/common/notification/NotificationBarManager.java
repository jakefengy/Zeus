package com.xm.zeus.common.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.xm.zeus.common.R;

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

    // 通知Id与通知类型管理
    private HashMap<NotificationType, List<Integer>> notificationTypeIdMap;
    private int notifyId; // 自增长

    // 通知栏提醒
    private boolean isTone = true, isVibration = true;
    private AudioManager am = null;
    private int currentSound;
    private Uri audioRes;

    public void init(Context appContext) {
        mNotificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationTypeIdMap = new HashMap<>();
        notifyId = 0;

        am = (AudioManager) appContext.getSystemService(appContext.AUDIO_SERVICE);
        currentSound = am.getStreamVolume(AudioManager.STREAM_SYSTEM);

        try {
            audioRes = Uri.parse("android.resource://com.xm.zeus.common/raw/line.mp3");
        } catch (Exception e) {
            e.printStackTrace();
            audioRes = null;
        }

    }

    private NotificationCompat.Builder getBuilder(Context appContext, String ticker, String contentTitle, String contentText, Uri sound, Class clazz, Bundle bundle, RemoteViews remoteviews) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext);

        builder.setWhen(System.currentTimeMillis())
                .setTicker(ticker)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_notify);

        if (remoteviews != null) {
            builder.setContent(remoteviews);
        } else {
            builder.setContentTitle(contentTitle)
                    .setContentText(contentText);
        }

        if (isTone) {
            if (sound != null) {
                playAudio(appContext, sound);
            } else {
                playAudio(appContext, audioRes);
            }
        }

        if (isVibration) {
            playShock(appContext);
        }

        if (clazz != null) {
            builder.setContentIntent(getDefaultIntent(appContext, notifyId, clazz, bundle));
        }

        return builder;
    }

    public boolean showChatNotify(Context appContext, String ticker, Uri sound, int shockLength, Class clazz, Bundle bundle, RemoteViews remoteviews) {

        if (TextUtils.isEmpty(ticker)) {
            return false;
        }

        if (remoteviews == null) {
            return false;
        }

        NotificationCompat.Builder mBuilder = getBuilder(appContext, ticker, "", "", sound, clazz, bundle, remoteviews);

        Notification notification = mBuilder.build();
        notification.contentView = remoteviews;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(notifyId, notification);

        recordNotifyId(NotificationType.CHAT, notifyId);

        notifyId++;

        return true;

    }

    public boolean showOtherNotify(Context appContext, String ticker, String contentTitle, String contentText, Uri sound, int shockLength, Class clazz, Bundle bundle) {
        if (TextUtils.isEmpty(ticker) || TextUtils.isEmpty(contentTitle) || TextUtils.isEmpty(contentText)) {
            return false;
        }

        NotificationCompat.Builder mBuilder = getBuilder(appContext, ticker, contentTitle, contentText, sound, clazz, bundle, null);

        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(notifyId, notification);

        recordNotifyId(NotificationType.OTHER, notifyId);

        notifyId++;

        return true;
    }

    public PendingIntent getDefaultIntent(Context appContext, int reqCode, Class clazz, Bundle bundle) {
        Intent intent = new Intent(appContext, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        Log.i("RemoteMsgTag", "Extras is " + bundle.toString());

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
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

    // play audio
    public void playAudio(final Context appContext, final Uri audioRes) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mPlayer = MediaPlayer.create(appContext, audioRes);
                mPlayer.setLooping(false);
                mPlayer.setVolume(currentSound, currentSound);

                mPlayer.start();
            }
        }).start();


    }

    // shock 0.2s
    public void playShock(final Context appContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Vibrator vib = (Vibrator) appContext.getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(200);
            }
        }).start();

    }

    // get set
    public boolean isTone() {
        return isTone;
    }

    public void setTone(boolean tone) {
        isTone = tone;
    }

    public boolean isVibration() {
        return isVibration;
    }

    public void setVibration(boolean vibration) {
        isVibration = vibration;
    }
}
