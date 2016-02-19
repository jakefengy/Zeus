package com.xm.zeus.base;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author fengy on 2016-02-19
 */
public abstract class BaseServerActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBindService(false);
    }

    // 推送服务相关

    private IBinder baseBinder;

    /**
     * 当这个IBinder所对应的Service进程被异常的退出时，比如被kill掉，系统调用。
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (baseBinder != null) {
                baseBinder.unlinkToDeath(mDeathRecipient, 0);
            }
            baseBinder = null;
            onReleaseService();
            onBindService(true);
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                if (service != null) {
                    baseBinder = service;
                    service.linkToDeath(mDeathRecipient, 0);
                }
                onConnectedService(name, service);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            onDisconnectedService(name);
        }
    };

    @Override
    protected void onDestroy() {
        onReleaseService();
        unbindService(connection);
        super.onDestroy();
    }

    /**
     * 绑定服务
     *
     * @param isReBind true：重新绑定服务，false：绑定服务
     */
    protected abstract void onBindService(boolean isReBind);

    /**
     * 成功绑定服务
     *
     * @param name
     * @param service
     */
    protected abstract void onConnectedService(ComponentName name, IBinder service);

    /**
     * 解除服务绑定
     *
     * @param name
     */
    protected abstract void onDisconnectedService(ComponentName name);

    /**
     * 释放服务相关资源。
     */
    protected abstract void onReleaseService();

}
