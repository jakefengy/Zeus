package com.xm.zeus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xm.zeus.service.TestRemoteMsgActivity;
import com.xm.zeus.service.ZeusRemoteService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("RemoteMsgTag", "MainActivity");
        startService(new Intent(MainActivity.this, ZeusRemoteService.class));

        startActivity(new Intent(MainActivity.this, TestRemoteMsgActivity.class));
        finish();

    }
}
