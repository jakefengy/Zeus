// IMsgManager.aidl
package com.xm.zeus.pushservice;

// Declare any non-default types here with import statements

import com.xm.zeus.pushservice.IMsgListener;

interface IMsgManager {
     List<String> getMsgs();

     void registerListener(IMsgListener listener);

     void unregisterListener(IMsgListener listener);
}
