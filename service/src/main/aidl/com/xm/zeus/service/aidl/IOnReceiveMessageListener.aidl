// IOnReceiveMessageListener.aidl
package com.xm.zeus.service.aidl;

import com.xm.zeus.service.aidl.MsgEntity;

// Declare any non-default types here with import statements

interface IOnReceiveMessageListener {
    void onReceiveMessage(in MsgEntity msg);
}
