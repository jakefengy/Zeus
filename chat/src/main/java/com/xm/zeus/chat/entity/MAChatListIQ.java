package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yuelongr on 2015/8/5.
 =================================================
 <iq type="get" id="juliet1" >
 <chatlist xmlns="urn:xmpp:archive:chatlist" />
 </iq>
 =================================================
 <iq type="result" id="juliet1" to="1@192.168.102.96/yuelongr-PC">
 <chatlist xmlns="urn:xmpp:archive:chatlist">
 <chat direction="from" lastmessageid="28" lastbody="eg" unreadcount="7" type="chat" withjid="2@192.168.102.96" lastdate="1439455885450" source="" firstdate="1439455884404" ordinal="0"/>
 <chat direction="from" lastmessageid="21" lastbody="dfgdf" unreadcount="18" type="groupchat" withjid="1@conference.192.168.102.96" lastdate="1439455882281" source="2@192.168.102.96" firstdate="1439455878177" ordinal="0"/>
 </chatlist>
 </iq>
 =================================================
 */
public class MAChatListIQ extends IQ {

    public static final String ELEMENT = "chatlist";
    public static final String NAMESPACE = "urn:xmpp:archive:chatlist";

    public static final String ATTRIBUTE_WITHJID = "withjid";
    public static final String ATTRIBUTE_STARTDATE = "startdate";
    public static final String ATTRIBUTE_ENDDATE = "enddate";
    public static final String ATTRIBUTE_PAGESIZE = "pagesize";
    public static final String ATTRIBUTE_PAGEINDEX = "pageindex";

    private final List<MAChatListItem> items = new ArrayList<MAChatListItem>();

    private String withJID;
    private Date startDate;
    private Date endDate;
    private int pageSize;
    private int pageIndex;

    public MAChatListIQ() {
        super(ELEMENT, NAMESPACE);
    }

    public List<MAChatListItem> getItems() {
        synchronized (items) {
            return Collections.unmodifiableList(new ArrayList<MAChatListItem>(items));
        }
    }

    public void addItem(MAChatListItem item) {
        synchronized (items) {
            items.add(item);
        }
    }

    public String getWithJID() {
        return withJID;
    }

    public void setWithJID(String withJID) {
        this.withJID = withJID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {

        if (withJID != null)
            xml.optAttribute(ATTRIBUTE_WITHJID, withJID);
        if (startDate != null)
            xml.optLongAttribute(ATTRIBUTE_STARTDATE, startDate.getTime());
        if (endDate != null)
            xml.optLongAttribute(ATTRIBUTE_ENDDATE, endDate.getTime());
        if (pageSize != 0)
            xml.optIntAttribute(ATTRIBUTE_PAGESIZE, pageSize);
        if (pageIndex != 0)
            xml.optIntAttribute(ATTRIBUTE_PAGEINDEX, pageIndex);

        xml.rightAngleBracket();

        synchronized (items) {
            for (MAChatListItem item : items) {
                xml.append(item.toXML());
            }
        }

        return xml;
    }

}
