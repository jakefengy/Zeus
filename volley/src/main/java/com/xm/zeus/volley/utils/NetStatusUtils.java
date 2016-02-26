package com.xm.zeus.volley.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * @author fengy on 2016-02-26
 */
public class NetStatusUtils {
    /**
     * 监听网络状态ActionString
     */
    public final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private CheckNetworkStatusReceiver mReceiver;

    private Context mContext;

    private OnNetworkStatusChangeListener mOnNetworkStatusChangeListener;

    public NetStatusUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 检查网络是否连接
     *
     * @return boolean true：连接；false：未连接
     */
    public boolean isConnected() {

        if (getActiveNetwork() == null) {
            return false;
        }

        return true;
    }

    /**
     * 获取当前网络连接类型
     *
     * @return ConnectivityManager 中定义的状态
     */
    public int getConnectionType() {

        NetworkInfo mNetworkInfo = getActiveNetwork();

        if (mNetworkInfo == null) {
            return -1;
        }

        return mNetworkInfo.getType();
    }

    /**
     * 注册网络状态监听广播
     *
     * @param mListener
     */
    public void registerNetworkStatusReceiver(
            OnNetworkStatusChangeListener mListener) {

        mOnNetworkStatusChangeListener = mListener;

        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_CHANGE_ACTION);
        mReceiver = new CheckNetworkStatusReceiver();
        mContext.registerReceiver(mReceiver, filter);
    }

    /**
     * 注销网络状态监听广播
     */
    public void unRegisterNetworkStatusReceiver() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
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
                NetworkInfo mNetworkInfo = getActiveNetwork();

                if (mNetworkInfo == null || !mNetworkInfo.isAvailable()) {
                    if (mOnNetworkStatusChangeListener != null) {
                        mOnNetworkStatusChangeListener.onDisconnect();
                    }
                    return;
                }

                if (mOnNetworkStatusChangeListener != null) {
                    mOnNetworkStatusChangeListener.onConnected(mNetworkInfo
                            .getType());
                }
                return;
            }
        }

    }

    /**
     * 自定义网络监听回调接口
     */
    public interface OnNetworkStatusChangeListener {

        /**
         * 网络断开的时候，被触发
         */
        void onDisconnect();

        /**
         * 网络连接、网络类型变化的时候被触发
         *
         * @param networkType 当前设备网络类型
         */
        void onConnected(int networkType);
    }

    private NetworkInfo getActiveNetwork() {

        ConnectivityManager mConnMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null) {
            return null;
        }
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();
        return aActiveInfo;
    }
}