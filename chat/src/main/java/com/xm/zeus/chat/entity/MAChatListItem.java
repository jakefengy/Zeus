package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

import java.util.Date;

/**
 * Created by yuelongr on 2015/8/5.
 */
public class MAChatListItem implements NamedElement {

    public static final String ELEMENT = "chat";

    public static final String ATTRIBUTE_WITHJID = "withjid";
    public static final String ATTRIBUTE_DIRECTION = "direction";
    public static final String ATTRIBUTE_LASTMESSAGEID = "lastmessageid";
    public static final String ATTRIBUTE_FIRSTDATE = "firstdate";
    public static final String ATTRIBUTE_LASTDATE = "lastdate";
    public static final String ATTRIBUTE_TYPE = "type";
    public static final String ATTRIBUTE_SOURCE = "source";
    public static final String ATTRIBUTE_LASTBODY = "lastbody";
    public static final String ATTRIBUTE_UNREADCOUNT = "unreadcount";
    public static final String ATTRIBUTE_ORDINAL = "ordinal";

    private final String withJID;
    private final String direction;
    private final long lastMessageID;
    private final Date firstDate;
    private final Date lastDate;
    private final String type;//chat,groupchat
    private final String source;
    private final String lastBody;
    private final int unReadCount;
    private final int ordinal;

    public MAChatListItem(String withJID, String direction, long lastMessageID, Date firstDate, Date lastDate, String type, String source, String lastBody, int unReadCount, int ordinal) {
        this.withJID = withJID;
        this.direction = direction;
        this.lastMessageID = lastMessageID;
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        this.type = type;
        this.source = source;
        this.lastBody = lastBody;
        this.unReadCount = unReadCount;
        this.ordinal = ordinal;
    }

    public String getWithJID() {
        return withJID;
    }

    public String getDirection() {
        return direction;
    }

    public long getLastMessageID() {
        return lastMessageID;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getLastBody() {
        return lastBody;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.optAttribute(ATTRIBUTE_WITHJID, getWithJID());
        xml.optAttribute(ATTRIBUTE_DIRECTION, getDirection());
        xml.optLongAttribute(ATTRIBUTE_LASTMESSAGEID, getLastMessageID());
        if(getFirstDate() != null)
            xml.optLongAttribute(ATTRIBUTE_FIRSTDATE, getFirstDate().getTime());
        if(getLastDate() != null)
            xml.optLongAttribute(ATTRIBUTE_LASTDATE, getLastDate().getTime());
        xml.optAttribute(ATTRIBUTE_TYPE, getType());
        xml.optAttribute(ATTRIBUTE_SOURCE, getSource());
        xml.optAttribute(ATTRIBUTE_LASTBODY, getLastBody());
        xml.optIntAttribute(ATTRIBUTE_UNREADCOUNT, getUnReadCount());
        xml.optIntAttribute(ATTRIBUTE_ORDINAL, getOrdinal());
        xml.rightAngleBracket();
        xml.closeElement(ELEMENT);
        return xml;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }


}
