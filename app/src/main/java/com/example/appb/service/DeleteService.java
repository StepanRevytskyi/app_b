package com.example.appb.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class DeleteService extends Service {

    final Uri CONTACT_URI = Uri.parse("content://com.example.appa.data/links");

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uri uri = CONTACT_URI;
                String[] url = {intent.getStringExtra("url")};
                getContentResolver().delete(uri, null, url);
            }
        }, 15000);
        return Service.START_STICKY;
    }
}
