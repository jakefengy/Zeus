package com.xm.zeus.ui;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xm.zeus.R;

public class BootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boot);

        Intent intent = new Intent();
        intent.setAction("com.xm.zeus.action.ZeusRemoteService");
        intent.putExtra("BinderTag", "msg");
        print("BootActivity.bindService");
        BootActivity.this.bindService(intent, conn, Service.BIND_AUTO_CREATE);

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            print("BootActivity.onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            print("BootActivity.onServiceDisconnected");
        }
    };


    protected void print(String content) {
        Log.i(TAG, content);
    }

    static final String TAG = "ClientTag";

}
