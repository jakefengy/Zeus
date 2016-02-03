package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

import java.util.Date;

public class MAMessageListItem implements NamedElement {

    public static final String ELEMENT = "msg";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_WITHJID = "withjid";
    public static final String ATTRIBUTE_DIRECTION = "direction";
    public static final String ATTRIBUTE_SENTDATE = "sentdate";

    private final long messageID;
    private String withJID;
    private final String direction;
    private final Date sentDate;
    private final String body;


    public MAMessageListItem(long messageID, String withJID, String direction, Date sentDate, String body) {
        this.messageID = messageID;
        this.withJID = withJID;
        this.direction = direction;
        this.sentDate = sentDate;
        this.body = body;
    }

    public long getMessageID() {
        return messageID;
    }

    public String getWithJID() {
        return withJID;
    }

    public String getDirection() {
        return direction;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public String getBody() {
        return body;
    }

    @Override
    public XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.optLongAttribute(ATTRIBUTE_ID, getMessageID());
        xml.optAttribute(ATTRIBUTE_WITHJID, getWithJID());
        xml.optAttribute(ATTRIBUTE_DIRECTION, getDirection());
        if(getSentDate() != null)
            xml.optLongAttribute(ATTRIBUTE_SENTDATE, getSentDate().getTime());
        xml.rightAngleBracket();
        xml.optAppend(getBody());
        xml.closeElement(ELEMENT);
        return xml;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }


}
