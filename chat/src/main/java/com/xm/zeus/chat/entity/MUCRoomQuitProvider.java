package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by yuelongr on 2015/8/5.
 */
public class MUCRoomQuitProvider extends IQProvider<MUCRoomQuitIQ> {

    @Override
    public MUCRoomQuitIQ parse(XmlPullParser parser, int initialDepth)
            throws XmlPullParserException, IOException {
        MUCRoomQuitIQ aMUCRoomQuitIQ = new MUCRoomQuitIQ();
        return aMUCRoomQuitIQ;
    }
}
