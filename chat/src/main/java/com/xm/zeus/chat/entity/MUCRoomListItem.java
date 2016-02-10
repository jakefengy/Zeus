package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by yuelongr on 2015/8/5.
 */
public class MUCRoomListItem implements NamedElement {

    public static final String ELEMENT = "room";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_NICKNAME = "nickname";
    public static final String ATTRIBUTE_AFFILIATION = "affiliation";

    private final String id;
    private final String name;
    private final String nickname;
    private final int affiliation;//owner(10),admin(20), member(30),outcast(40),none(50);

    public MUCRoomListItem(String id, String name, String nickname, int affiliation) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.affiliation = affiliation;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
            return nickname;
        }

    public int getAffiliation() {
        return affiliation;
    }

    @Override
    public XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.optAttribute(ATTRIBUTE_ID, getId());
        xml.optAttribute(ATTRIBUTE_NAME, getName());
        xml.optAttribute(ATTRIBUTE_NICKNAME, getNickname());
        xml.optIntAttribute(ATTRIBUTE_AFFILIATION, getAffiliation());
        xml.rightAngleBracket();
        xml.closeElement(ELEMENT);
        return xml;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }


}
