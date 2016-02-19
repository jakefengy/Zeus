package com.xm.zeus.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xm.zeus.service.ZeusRemoteService;

/**
 * 手机状态监听，重启push service.
 *
 * @author fengy on 2016-02-19
 */
public class RebirthReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.i("RemoteMsgTag", "RebirthReceiver.onReceive = " + intent.getAction() + " & startService");
            context.startService(new Intent(context, ZeusRemoteService.class));
        } else {
            Log.i("RemoteMsgTag", "RebirthReceiver.onReceive = null");
        }
    }

}
