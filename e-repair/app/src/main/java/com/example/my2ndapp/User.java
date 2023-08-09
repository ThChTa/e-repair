package com.example.my2ndapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.common.reflection.qual.NewInstance;

public class User extends AppCompatActivity {

     private Button logoutU;
     Button rv;
     TextView tv;

     private String emailFromSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        rv = findViewById(R.id.recView);
        logoutU = findViewById(R.id.logoutUser);
        tv = findViewById(R.id.aa);

        Intent intent = getIntent();
        emailFromSignIn = intent.getExtras().getString("emailFromSignIn");
        tv.setText(emailFromSignIn);


        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                //startActivity(new Intent(getApplicationContext(), MyPublications.class));
                Intent i = new Intent(User.this, MyPublications.class);
                i.putExtra("emailFromUser", emailFromSignIn);
                startActivity(i);
                finish();
            }
        });

        logoutU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        });


    }
}