package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MAMessageListIQ extends IQ {

    public static final String ELEMENT = "msglist";
    public static final String NAMESPACE = "urn:xmpp:archive:msglist";

    public static final String ATTRIBUTE_WITHJID = "withjid";
    public static final String ATTRIBUTE_STARTDATE = "startdate";
    public static final String ATTRIBUTE_ENDDATE = "enddate";
    public static final String ATTRIBUTE_PAGESIZE = "pagesize";
    public static final String ATTRIBUTE_PAGEINDEX = "pageindex";

    private final List<MAMessageListItem> items = new ArrayList<MAMessageListItem>();

    private String withJID;
    private Date startDate;
    private Date endDate;
    private int pageSize;
    private int pageIndex;

    public MAMessageListIQ() {
        super(ELEMENT, NAMESPACE);
    }

    public MAMessageListIQ(String withJID) {
        super(ELEMENT, NAMESPACE);
        this.withJID = withJID;
    }

    public List<MAMessageListItem> getItems() {
        synchronized (items) {
            return Collections.unmodifiableList(new ArrayList<MAMessageListItem>(items));
        }
    }

    public void addItem(MAMessageListItem item) {
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
            for (MAMessageListItem item : items) {
                xml.append(item.toXML());
            }
        }

        return xml;
    }

}
