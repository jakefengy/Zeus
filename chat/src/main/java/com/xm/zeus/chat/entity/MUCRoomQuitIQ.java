package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.IQ;

/**
 * Created by yuelongr on 2015/8/4.
 =================================================
 =================================================
 <iq id='HsryZ-40' type='get'>
 <quit xmlns='users:muc:room:quit'roomjid='1@conference.192.168.102.96' />
 </iq>
 =================================================
 =================================================
 */
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
