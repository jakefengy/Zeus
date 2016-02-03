package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.IQ;

public class MUCRoomQuitIQ extends IQ {

    public static final String ELEMENT = "quit";
    public static final String NAMESPACE = "users:muc:room:quit";

    public static final String ATTRIBUTE_ROOMJID = "roomjid";

    private String roomJID;

    public MUCRoomQuitIQ() {
        super(ELEMENT, NAMESPACE);
    }

    public MUCRoomQuitIQ(String roomJID) {
        super(ELEMENT, NAMESPACE);
        this.roomJID = roomJID;
    }

    public String getRoomJID() {
        return roomJID;
    }

    public void setRoomJID(String roomJID) {
        this.roomJID = roomJID;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        if (roomJID != null)
            xml.optAttribute(ATTRIBUTE_ROOMJID, roomJID);

        xml.rightAngleBracket();

        return xml;
    }

}
