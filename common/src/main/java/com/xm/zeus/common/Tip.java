package com.xm.zeus.common;

import android.content.Context;
import android.widget.Toast;

/**
 * 提醒...
 */
public class Tip {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
