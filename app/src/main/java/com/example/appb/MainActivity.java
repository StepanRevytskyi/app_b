package com.example.appb;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ImageView;

import com.example.appb.service.DeleteService;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final int MILIS_IN_FUTURE = 10000;
    private final int COUNT_INTERVAL = 1000;
    private long time;

    private ImageView mImageView;

    final Uri CONTACT_URI = Uri.parse("content://com.example.appa.data/links");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);
        time = new Date().getTime();

        Intent intent = getIntent();
        if (intent.getStringExtra("url") != null) {
            if (intent.getStringExtra("type").equals("ok")) {
                String url = intent.getStringExtra("url");
                String status = null;
                if (Patterns.WEB_URL.matcher(url).matches()) {
                    status = "1";
                } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                    status = "2";
                } else {
                    status = "3";
                }
                time = time - intent.getLongExtra("time", 0);
                String timeOpen = String.valueOf(time);

                insertData(url, status, timeOpen);
                Picasso.get()
                        .load(url)
                        .into(mImageView);
            } else {
                if (intent.getStringExtra("status").equals("1")) {
                    String url = intent.getStringExtra("url");
                    Picasso.get()
                            .load(url)
                            .into(mImageView);

//                    not working
//                    delete(url);
                }
            }

        } else {
            showErrorAlertDialog();
        }
    }

    private void delete(String url){
        startService(new Intent(MainActivity.this, DeleteService.class).putExtra("url", url));
    }

    private void insertData(String url, String status, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("link", url);
        contentValues.put("status", status);
        contentValues.put("time_to_open", time);
        Uri uri = getContentResolver().insert(CONTACT_URI, contentValues);
    }

    private void showErrorAlertDialog() {
        final int secondsToClose = MILIS_IN_FUTURE / COUNT_INTERVAL;

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.error_title));
        alertDialog.setMessage(getString(R.string.error_message, secondsToClose));
        alertDialog.setCancelable(false);
        alertDialog.show();

        new CountDownTimer(MILIS_IN_FUTURE, COUNT_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timeLeft = (int) (millisUntilFinished / COUNT_INTERVAL);
                alertDialog.setMessage(getString(R.string.error_message, timeLeft));
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }
}
