package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.IQ;

public class MAChatModifyIQ extends IQ {

    public static final String ELEMENT = "modify";
    public static final String NAMESPACE = "urn:xmpp:archive:modify";

    public static final String ATTRIBUTE_WITHJID = "withjid";
    public static final String ATTRIBUTE_ACTION = "action";
    public static final String ATTRIBUTE_UNREADCOUNT = "unreadcount";
    public static final String ATTRIBUTE_ORDINAL = "ordinal";

    public enum Action {
        //common action
        common,
        //set unreadcount
        unreadcount,
        //set ordinal
        ordinal,
        //delete
        delete
    }

    private String withJID;
    private Action action = Action.common;
    private int unReadCount = -1;
    private int ordinal = -1;


    public MAChatModifyIQ() {
        super(ELEMENT, NAMESPACE);
    }

    public MAChatModifyIQ(String withJID, Action action) {
        super(ELEMENT, NAMESPACE);
        this.setType(Type.set);
        this.withJID = withJID;
        this.action = action;
        if (action == Action.unreadcount)
            this.unReadCount = 0;
        if (action == Action.ordinal)
            this.ordinal = 0;
    }

    public String getWithJID() {
        return withJID;
    }

    public void setWithJID(String withJID) {
        this.withJID = withJID;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        if (withJID != null)
            xml.optAttribute(ATTRIBUTE_WITHJID, withJID);
        if (action != Action.common)
            xml.optAttribute(ATTRIBUTE_ACTION, action.toString());
        if (unReadCount != -1)
            xml.optIntAttribute(ATTRIBUTE_UNREADCOUNT, unReadCount);
        if (ordinal != -1)
            xml.optIntAttribute(ATTRIBUTE_ORDINAL, ordinal);

        xml.rightAngleBracket();

        return xml;
    }

}
