package com.xm.zeus.chat.entity;

/**
 * 作者：小孩子xm on 2016-02-01 14:38
 * 邮箱：1065885952@qq.com
 */
public class BizMessageContent {

    // Message Type
    private String Type;

    // Text Content
    private String Text;

    // Image Content
    private String ImageId;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

}
