package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MUCRoomListIQ extends IQ {

    public static final String ELEMENT = QUERY_ELEMENT;
    public static final String NAMESPACE = "users:muc:room:list";

    private final List<MUCRoomListItem> items = new ArrayList<MUCRoomListItem>();

    public MUCRoomListIQ() {
        super(ELEMENT, NAMESPACE);
    }

    public List<MUCRoomListItem> getItems() {
        synchronized (items) {
            return Collections.unmodifiableList(new ArrayList<MUCRoomListItem>(items));
        }
    }

    public void addItem(MUCRoomListItem item) {
        synchronized (items) {
            items.add(item);
        }
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();

        synchronized (items) {
            for (MUCRoomListItem item : items) {
                xml.append(item.toXML());
            }
        }

        return xml;
    }

}
