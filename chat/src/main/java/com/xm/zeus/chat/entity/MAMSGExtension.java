package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

import java.util.Date;

/**
 * Created by yuelongr on 2015/9/2.
 */
public class MAMSGExtension implements ExtensionElement {

    public static final String NAMESPACE = "urn:xmpp:archive:mamsgextension";
    public static final String ELEMENT_MSGEXT = "msgext";

    public static final String ELEMENT_FROMJID = "fromjid";
    public static final String ELEMENT_TOJID = "tojid";
    public static final String ELEMENT_BODY = "body";
    public static final String ELEMENT_DATE = "date";
    public static final String ELEMENT_MESSAGEID = "messageid";

    private String fromJID;
    private String toJID;
    private String body;
    private Date date;
    private long messageID;

    public String getFromJID() {
        return fromJID;
    }

    public void setFromJID(String fromJID) {
        this.fromJID = fromJID;
    }

    public String getToJID() {
        return toJID;
    }

    public void setToJID(String toJID) {
        this.toJID = toJID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT_MSGEXT;
    }

    @Override
    public CharSequence toXML() {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.rightAngleBracket();

        xml.optElement(ELEMENT_FROMJID, getFromJID());
        xml.optElement(ELEMENT_TOJID, getToJID());
        xml.optElement(ELEMENT_BODY, getBody());
        if(getDate() != null)
            xml.optElement(ELEMENT_DATE, String.valueOf(getDate().getTime()));
        xml.optElement(ELEMENT_MESSAGEID, String.valueOf(getMessageID()));

        xml.closeElement(ELEMENT_MSGEXT);
        return xml;
    }
}
