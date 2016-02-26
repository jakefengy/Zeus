package com.xm.zeus.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * @author fengy on 2016-02-26
 */
public abstract class AppBaseActivity extends BaseActivity {

    public final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private CheckNetworkStatusReceiver mReceiver = new CheckNetworkStatusReceiver();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void registerNetStatusReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_CHANGE_ACTION);
        registerReceiver(mReceiver, filter);
    }

    public void unRegisterNetStatusReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    /**
     * 自定义广播接收类
     */
    private class CheckNetworkStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handleIntent(intent);
        }

        private void handleIntent(Intent intent) {
            String action = intent.getAction();

            if (TextUtils.equals(action, CONNECTIVITY_CHANGE_ACTION)) {// 网络变化的时候会发送通知
//                NetworkInfo mNetworkInfo = getActiveNetwork();
//
//                if (mNetworkInfo == null || !mNetworkInfo.isAvailable()) {
//                    onNetDisconnect();
//                    return;
//                }
//
//                onNetConnect(mNetworkInfo.getType());
            }
        }

    }

    protected void onNetConnect(int netType) {

    }

    protected void onNetDisconnect() {

    }

}
