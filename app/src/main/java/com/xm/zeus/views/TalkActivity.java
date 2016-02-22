package com.xm.zeus.views;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import com.xm.zeus.R;
import com.xm.zeus.base.BaseServerActivity;
import com.xm.zeus.chat.utils.ChatClient;
import com.xm.zeus.service.ZeusRemoteService;

public class TalkActivity extends BaseServerActivity {

    //
    private TextView textView;

    private ChatClient chatClient;

    @Override
    protected void initVariables() {
        print("TalkActivity.initVariables()");
        chatClient = ChatClient.getInstance();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_talk);
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected Intent setServiceIntent() {
        return new Intent(TalkActivity.this, ZeusRemoteService.class);
    }

    @Override
    protected void onConnectedService(ComponentName name, IBinder service) {

    }

    @Override
    protected void onDisconnectedService(ComponentName name) {

    }

    @Override
    protected void onReleaseService() {

    }

    private void print(String content) {

    }


}
