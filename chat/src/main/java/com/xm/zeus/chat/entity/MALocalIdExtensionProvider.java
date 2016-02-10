package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by yuelongr on 2015/9/2.
 */
public class MALocalIdExtensionProvider extends ExtensionElementProvider<MALocalIdExtension> {
    @Override
    public MALocalIdExtension parse(XmlPullParser parser, int initialDepth) {

        MALocalIdExtension maLocalIdExtension = new MALocalIdExtension();
        try {
            boolean done = false;
            int eventType = parser.next();
            while (!done && eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals(MALocalIdExtension.ELEMENT_EXT_LOCAL_VALUE)) {
                        maLocalIdExtension.setLocalid(parser.nextText());
                        done = true;
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maLocalIdExtension;
    }

}
