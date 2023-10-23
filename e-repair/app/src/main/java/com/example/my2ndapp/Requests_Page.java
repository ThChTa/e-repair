package com.example.my2ndapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Requests_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page);

        TextView tv = (TextView)findViewById(R.id.Type);

        String receivedKey = getIntent().getStringExtra("key");
        tv.setText(receivedKey);



    }
}