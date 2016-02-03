package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

public class MAChatListProvider extends IQProvider<MAChatListIQ> {

    @Override
    public MAChatListIQ parse(XmlPullParser parser, int initialDepth)
            throws XmlPullParserException, IOException {
        MAChatListIQ aMAChatListIQ = new MAChatListIQ();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(MAChatListItem.ELEMENT)) {
                    String with = parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_WITHJID);
                    String direction = parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_DIRECTION);
                    long lastMessageID = Long.parseLong(parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_LASTMESSAGEID));
                    Date firstDate = new Date(Long.parseLong(parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_FIRSTDATE)));
                    Date lastDate = new Date(Long.parseLong(parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_LASTDATE)));
                    String type = parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_TYPE);
                    String source = parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_SOURCE);
                    String lastBody = parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_LASTBODY);
                    int unReadCount = Integer.parseInt(parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_UNREADCOUNT));
                    int ordinal = Integer.parseInt(parser.getAttributeValue("", MAChatListItem.ATTRIBUTE_ORDINAL));

                    aMAChatListIQ.addItem(new MAChatListItem(with, direction, lastMessageID, firstDate, lastDate, type, source, lastBody, unReadCount, ordinal));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(MAChatListIQ.ELEMENT)) {
                    done = true;
                }
            }
        }

        return aMAChatListIQ;
    }
}
