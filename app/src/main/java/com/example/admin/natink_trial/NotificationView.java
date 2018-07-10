package com.example.admin.natink_trial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class NotificationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        Toast.makeText(getApplicationContext(),"Notification Clicked ",Toast.LENGTH_SHORT).show();
    }
}
