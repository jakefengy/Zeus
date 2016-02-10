package com.xm.zeus.chat.entity;

import org.jivesoftware.smack.packet.IQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yuelongr on 2015/8/5.
 =================================================
 <iq type="get" id="page1" >
 <msglist xmlns="urn:xmpp:archive:msglist" withjid="2@192.168.102.96" />
 </iq>
 =================================================
 <iq type="result" id="page1" to="1@192.168.102.96/yuelongr-PC">
 <msglist xmlns="urn:xmpp:archive:msglist">
 <msg direction="to" sentdate="1439455860788" id="1" withjid="2@192.168.102.96">1-211111111111111111111</msg>
 <msg direction="to" sentdate="1439455864939" id="2" withjid="2@192.168.102.96">1-22222222222222222</msg>
 <msg direction="to" sentdate="1439455873594" id="3" withjid="1@conference.192.168.102.96">sdfsfasfasdf sf </msg>
 <msg direction="to" sentdate="1439545893728" id="29" withjid="1@conference.192.168.102.96">123</msg>
 <msg direction="to" sentdate="1439546196945" id="30" withjid="2@192.168.102.96">test 离线消息~</msg>
 <msg direction="from" sentdate="1439546244854" id="31" withjid="2@192.168.102.96">2回复1</msg>
 <msg direction="to" sentdate="1439546288547" id="32" withjid="2@192.168.102.96">sdfsdf</msg>
 <msg direction="to" sentdate="1439954562639" id="33" withjid="2@192.168.102.96">1</msg>
 <msg direction="to" sentdate="1439954601399" id="34" withjid="1@conference.192.168.102.96">12</msg>
 <msg direction="to" sentdate="1439954888711" id="35" withjid="2@192.168.102.96">1</msg>
 <msg direction="to" sentdate="1439954935071" id="36" withjid="1@conference.192.168.102.96">1</msg>
 </msglist>
 </iq>
 =================================================
 */
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
