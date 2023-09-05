package com.example.my2ndapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.reflection.qual.NewInstance;

public class Admin extends AppCompatActivity {

    private Button logoutAdmin;
    Button rv;
    TextView tv;

    private String emailFromSignIn,emailFromPublications, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        rv = findViewById(R.id.recView);
        tv = findViewById(R.id.aa);
        logoutAdmin = findViewById(R.id.logoutA);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("emailFromSignIn")) {
            emailFromSignIn = intent.getStringExtra("emailFromSignIn");
            if (emailFromSignIn != null) {
                //tv.setText(emailFromSignIn);
            } else {
                // Handle the case where emailFromSignIn is null
            }
        } else {
            emailFromPublications = intent.getStringExtra("emailFromPublications");
            if (emailFromPublications != null) {
                tv.setText(emailFromPublications);
                emailFromSignIn = emailFromPublications;
            }

            // Handle the case where the intent or the extra doesn't exist
        }



        //set query DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("Users");

        //query starts
        query.orderByChild("email").equalTo(emailFromSignIn).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    name = (String) jobSnapshot.child("fn").getValue();//get first name to search for publications

                    tv.setText("Good to see you " +name + "!");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage()); //print if error exists
            }
        });  //query ends



        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                //startActivity(new Intent(getApplicationContext(), MyPublications.class));
                Intent i = new Intent(Admin.this, Publications.class);
                i.putExtra("emailFromAdmin", emailFromSignIn);
                startActivity(i);
                finish();
            }
        });

        logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            }
        });


    }
}