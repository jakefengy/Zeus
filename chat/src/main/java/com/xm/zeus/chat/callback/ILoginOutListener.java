package com.xm.zeus.chat.callback;

/**
 * 登出回调
 * <p>
 * 作者：小孩子xm on 2016-02-01 11:36
 * 邮箱：1065885952@qq.com
 */
public interface ILoginOutListener {

    void onLoginOutSuccessful();

    void onLoginOutFailed(Exception e);

}
