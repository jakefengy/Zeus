package com.xm.zeus.chat.callback;

/**
 * 登录回调
 * <p/>
 * 作者：小孩子xm on 2016-01-31 14:31
 * 邮箱：1065885952@qq.com
 */
public interface ILoginListener {

    void onLoginSuccessful();

    void onLoginFailed(Exception e);

}
