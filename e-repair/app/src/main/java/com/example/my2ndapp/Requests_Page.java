package com.example.my2ndapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Requests_Page extends AppCompatActivity {

    ImageButton backButton;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page);

        backButton = (ImageButton) findViewById(R.id.imageButtonRequestsPageActivity);


        Intent intent = getIntent();
        String emailFromMyPublications = intent.getExtras().getString("emailFromMyPublications");  //get email from MyPublications


        backButton.setOnClickListener(new View.OnClickListener() {   //when back btn is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Requests_Page.this, MyPublications.class);
                i.putExtra("emailFromRequestsPage", emailFromMyPublications);   //emailFromRequestsPage = emailFromMyPublications, so i have one variable
                startActivity(i);
            }
        });

    }
}