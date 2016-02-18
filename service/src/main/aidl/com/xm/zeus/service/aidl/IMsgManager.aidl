// IMsgManager.aidl
package com.xm.zeus.service.aidl;

import com.xm.zeus.service.aidl.MsgEntity;
import com.xm.zeus.service.aidl.IOnReceiveMessageListener;

// Declare any non-default types here with import statements

interface IMsgManager {
    List<MsgEntity> getMsgList();
    void registerListener(IOnReceiveMessageListener listener);
    void unregisterListener(IOnReceiveMessageListener listener);

}
