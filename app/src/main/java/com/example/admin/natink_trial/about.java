package com.example.admin.natink_trial;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class about extends AppCompatActivity {
    private WebView wvAboutUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        wvAboutUs = (WebView) findViewById(R.id.about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wvAboutUs.loadUrl("file:///android_asset/about.html");
        wvAboutUs.clearCache(true);
        wvAboutUs.clearHistory();
        wvAboutUs.getSettings().setJavaScriptEnabled(true);
        wvAboutUs.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    }

}
