package com.xm.zeus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xm.zeus.views.BootActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.xm.zeus.action.ZeusRemoteService");
        serviceIntent.putExtra("StartTag", "MainActivity");
        startService(serviceIntent);

        Intent intent = new Intent(MainActivity.this, BootActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}
