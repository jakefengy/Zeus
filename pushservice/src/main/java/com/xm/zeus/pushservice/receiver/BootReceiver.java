package com.xm.zeus.pushservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xm.zeus.common.Logger;
import com.xm.zeus.pushservice.ZeusRemoteService;

/**
 * 手机状态监听，重启push service.
 *
 * @author fengy on 2016-02-19
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            print("BootReceiver.onReceive = " + intent.getAction() + " & startService");
            Intent serviceIntent = new Intent(context, ZeusRemoteService.class);
            serviceIntent.putExtra("StartTag", "BootReceiver");
            context.startService(serviceIntent);
        } else {
            print("BootReceiver.onReceive = null");
        }
    }

    protected void print(String content) {
        Log.i("ReceiverTag", content);
        try {
            Logger.writeLog("ReceiverTag" + " : " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
