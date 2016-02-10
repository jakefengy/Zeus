package com.xm.zeus.chat.callback;

import com.xm.zeus.chat.entity.MAChatListItem;

import java.util.List;

/**
 * 作者：小孩子xm on 2016-02-01 13:19
 * 邮箱：1065885952@qq.com
 */
public interface IRecentContactListener {

    void onSuccessful(List<MAChatListItem> recentList);

    void onError(Exception e);

}
