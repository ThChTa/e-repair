package com.example.my2ndapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class MyProfileUser extends AppCompatActivity {

    ImageButton backButton;
    EditText firstNameGap, lastNameGap, typeGap, emailGap;
    Button buttonUpdateProfile;
    String userKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_user);

        backButton = (ImageButton) findViewById(R.id.backButtonMyProfileUser);
        firstNameGap = findViewById(R.id.FirstNameGap);
        lastNameGap = findViewById(R.id.LastNameGap);
        typeGap = findViewById(R.id.typeGap);
        emailGap = findViewById(R.id.emailGap);
        buttonUpdateProfile = (Button)findViewById(R.id.buttonUpdateProfile);

        Intent intent = getIntent();
        String emailFromUser = intent.getExtras().getString("emailFromUser");  //email when navigate from User



        backButton.setOnClickListener(new View.OnClickListener() {   //when back btn is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProfileUser.this, User.class);
                i.putExtra("emailFromMyProfileUser", emailFromUser);   //if i come from User.class
                startActivity(i);
            }
        });



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(emailFromUser);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Loop through the matching nodes
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String fn = userSnapshot.child("fn").getValue(String.class);
                        String ln = userSnapshot.child("ln").getValue(String.class);

                        // You can handle multiple users here, but if you expect only one user, you can break the loop
                        firstNameGap.setText(fn);
                        lastNameGap.setText(ln);
                        typeGap.setText("User");
                        emailGap.setText(emailFromUser);

                        userKey = userSnapshot.getKey();

                        break;



                    }
                } else {
                    // Handle the case where the "Users" data with the specified email doesn't exist.
                }

                buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userKey != null) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("fn", firstNameGap.getText().toString());
                            map.put("ln", lastNameGap.getText().toString());
                            map.put("email", emailGap.getText().toString());




                            FirebaseDatabase.getInstance().getReference().child("Users").child(userKey).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    });

                        }
                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }


        });
















    }
}