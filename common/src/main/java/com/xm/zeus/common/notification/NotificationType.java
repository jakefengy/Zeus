package com.xm.zeus.common.notification;

/**
 * 通知类型
 *
 * @author fengy on 2016-02-17
 */
public enum NotificationType {


    CHAT {
        public String getCode() {
            return "chat";
        }

        public String getName() {
            return "聊天";
        }
    };

    public abstract String getCode();

    public abstract String getName();
}
