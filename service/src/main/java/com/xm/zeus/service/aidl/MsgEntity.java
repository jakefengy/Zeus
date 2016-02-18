package com.xm.zeus.service.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import com.xm.zeus.common.notification.NotificationType;

/**
 * @author fengy on 2016-02-18
 */
public class MsgEntity implements Parcelable {

    private NotificationType Type;
    private String Content;

    public MsgEntity(NotificationType type, String content) {
        Type = type;
        Content = content;
    }

    public NotificationType getType() {
        return Type;
    }

    public void setType(NotificationType type) {
        Type = type;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return String.format("[Type:%s, Content:%s]", Type.getCode(), Content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Type == null ? -1 : this.Type.ordinal());
        dest.writeString(this.Content);
    }

    public MsgEntity() {
    }

    protected MsgEntity(Parcel in) {
        int tmpType = in.readInt();
        this.Type = tmpType == -1 ? null : NotificationType.values()[tmpType];
        this.Content = in.readString();
    }

    public static final Parcelable.Creator<MsgEntity> CREATOR = new Parcelable.Creator<MsgEntity>() {
        public MsgEntity createFromParcel(Parcel source) {
            return new MsgEntity(source);
        }

        public MsgEntity[] newArray(int size) {
            return new MsgEntity[size];
        }
    };
}
