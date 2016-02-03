package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

public class MAMessageListProvider extends IQProvider<MAMessageListIQ> {

    @Override
    public MAMessageListIQ parse(XmlPullParser parser, int initialDepth)
            throws XmlPullParserException, IOException {
        MAMessageListIQ aMAMessageListIQ = new MAMessageListIQ();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(MAMessageListItem.ELEMENT)) {
                    long messageID = Long.parseLong(parser.getAttributeValue("", MAMessageListItem.ATTRIBUTE_ID));
                    String withJID = parser.getAttributeValue("", MAMessageListItem.ATTRIBUTE_WITHJID);
                    String direction = parser.getAttributeValue("", MAMessageListItem.ATTRIBUTE_DIRECTION);
                    Date sentDate = new Date(Long.parseLong(parser.getAttributeValue("", MAMessageListItem.ATTRIBUTE_SENTDATE)));
                    String body = parser.nextText();

                    aMAMessageListIQ.addItem(new MAMessageListItem(messageID, withJID, direction, sentDate, body));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(MAMessageListIQ.ELEMENT)) {
                    done = true;
                }
            }
        }

        return aMAMessageListIQ;
    }
}
