package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class MALocalIdExtension implements ExtensionElement {

    public static final String NAMESPACE = "urn:xmpp:archive:mamsglocal";
    public static final String ELEMENT_EXT_LOCALID = "local";
    public static final String ELEMENT_EXT_LOCAL_VALUE = "id";

    private String localid;

    public String getLocalid() {
        return localid;
    }

    public void setLocalid(String localid) {
        this.localid = localid;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT_EXT_LOCALID;
    }

    @Override
    public CharSequence toXML() {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.rightAngleBracket();

        xml.optElement(ELEMENT_EXT_LOCAL_VALUE, getLocalid());

        xml.closeElement(ELEMENT_EXT_LOCALID);

        return xml;
    }

    public static void main(String[] args) {

        MALocalIdExtension extension = new MALocalIdExtension();
        extension.setLocalid("AAA");
        String xml = String.valueOf(extension.toXML());

        System.out.println(xml);

    }

}
