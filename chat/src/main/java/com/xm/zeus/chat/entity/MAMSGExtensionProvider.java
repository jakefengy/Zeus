package com.xm.zeus.chat.entity;

import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

public class MAMSGExtensionProvider extends ExtensionElementProvider<MAMSGExtension> {
    @Override
    public MAMSGExtension parse(XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException, SmackException {

        MAMSGExtension mamsgExtension = new MAMSGExtension();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            Log.i("MsgExt2", "eventType = " + eventType + " , name = " + parser.getName());
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals(MAMSGExtension.ELEMENT_FROMJID)) {
                    mamsgExtension.setFromJID(parser.nextText());
                } else if (parser.getName().equals(MAMSGExtension.ELEMENT_TOJID)) {
                    mamsgExtension.setToJID(parser.nextText());
                } else if (parser.getName().equals(MAMSGExtension.ELEMENT_BODY)) {
                    mamsgExtension.setBody(parser.nextText());
                } else if (parser.getName().equals(MAMSGExtension.ELEMENT_DATE)) {
                    mamsgExtension.setDate(new Date(Long.parseLong(parser.nextText())));
                } else if (parser.getName().equals(MAMSGExtension.ELEMENT_MESSAGEID)) {
                    mamsgExtension.setMessageID(Long.parseLong(parser.nextText()));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(MAMSGExtension.ELEMENT_MSGEXT)) {
                    done = true;
                }
            }
        }

        return mamsgExtension;
    }
}
