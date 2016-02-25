// IMsgManager.aidl
package com.xm.zeus.common.aidl;

// Declare any non-default types here with import statements

import com.xm.zeus.common.aidl.IMsgListener;

interface IMsgManager {
     List<String> getMsgs();

     void registerListener(IMsgListener listener);

     void unregisterListener(IMsgListener listener);
}
