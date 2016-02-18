package com.xm.zeus.service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowMsgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg);

        Bundle bundle = getIntent().getExtras();

        Log.i("RemoteMsgTag", "ShowMsgActivity.onCreate " + bundle.getString("Msg"));

        ((TextView) findViewById(R.id.msg)).setText(bundle.getString("Msg"));

    }
}
