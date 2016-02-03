package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MUCRoomListProvider extends IQProvider<MUCRoomListIQ> {

    @Override
    public MUCRoomListIQ parse(XmlPullParser parser, int initialDepth)
            throws XmlPullParserException, IOException {
        MUCRoomListIQ aMUCRoomListIQ = new MUCRoomListIQ();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(MUCRoomListItem.ELEMENT)) {
                    String id = parser.getAttributeValue("", MUCRoomListItem.ATTRIBUTE_ID);
                    String name = parser.getAttributeValue("", MUCRoomListItem.ATTRIBUTE_NAME);
                    String nickname = parser.getAttributeValue("", MUCRoomListItem.ATTRIBUTE_NICKNAME);
                    int affiliation = Integer.parseInt(parser.getAttributeValue("", MUCRoomListItem.ATTRIBUTE_AFFILIATION));

                    aMUCRoomListIQ.addItem(new MUCRoomListItem(id, name, nickname, affiliation));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(MUCRoomListIQ.ELEMENT)) {
                    done = true;
                }
            }
        }

        return aMUCRoomListIQ;
    }
}
