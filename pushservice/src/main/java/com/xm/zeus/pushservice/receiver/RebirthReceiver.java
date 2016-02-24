package com.xm.zeus.pushservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xm.zeus.pushservice.ZeusRemoteService;

/**
 * 手机状态监听，重启push service.
 *
 * @author fengy on 2016-02-19
 */
public class RebirthReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.i("ReceiverTag", "BootReceiver.onReceive = " + intent.getAction() + " & startService");
            context.startService(new Intent(context, ZeusRemoteService.class));
        } else {
            Log.i("ReceiverTag", "BootReceiver.onReceive = null");
        }
    }

}
