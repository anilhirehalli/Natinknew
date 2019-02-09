package com.example.admin.natink_trial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class mainthis extends AppCompatActivity {
    int initial1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainthis);
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }


            @Override
            public void onFinish() {
                //set the new Content of your activity
                checkvalue();
                //  setvalue();
                checkvalue();
                // Toast.makeText(getApplicationContext(),"initaial : "+initial1,Toast.LENGTH_SHORT).show();
                // Toast.makeText(getApplicationContext(),initial1+" : initial",Toast.LENGTH_SHORT).show();
                if(initial1==0)
                {
                   /* PrefManager prefManager = new PrefManager(getApplicationContext());

                    // make first time launch TRUE
                    prefManager.setFirstTimeLaunch(true);*/

                    startActivity(new Intent(mainthis.this, licence.class));
                    finish();

                }

                else {
                    Intent intent = new Intent(mainthis.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }.start();
    }
    private void checkvalue() {
        SharedPreferences settings1 = getSharedPreferences("install", 0);
        initial1 = settings1.getInt("install1", 0);
    }
}
