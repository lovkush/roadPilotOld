package com.lucky.roadpilot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class InsuranceActivity extends AppCompatActivity {

    String value;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);

        Intent intent = getIntent();
        value = intent.getStringExtra("Doc");

        web = findViewById(R.id.web);
        web.loadUrl(value);

    }
}