package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.my2ndapp.DataAdapter;
import com.example.my2ndapp.MyPublications;
import com.example.my2ndapp.R;
import com.example.my2ndapp.RecyclerViewData;
import com.example.my2ndapp.SignIn;
import com.example.my2ndapp.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText type,location,description;
    Button btnAdd;
    ImageButton imageButton;
    String firstNameFromMyPublications, emailFromMyPublicationsToAddActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        firstNameFromMyPublications = intent.getExtras().getString("firstNameFromMyPublications");  //get first name from MyPublications
        emailFromMyPublicationsToAddActivity = intent.getExtras().getString("emailFromMyPublications"); //get email from MyPublications to send it back when back btn is clicked

        //Log.d("sos2","firstNameFromMyPublications = " + firstNameFromMyPublications + ", emailFromMyPublicationsToAddActivity = " + emailFromMyPublicationsToAddActivity);

        imageButton = (ImageButton) findViewById(R.id.imageButtonAddActivity);

        type = (EditText)findViewById(R.id.addType);
        location = (EditText)findViewById(R.id.addLocation);
        description = (EditText)findViewById(R.id.addDescription);


        btnAdd = (Button)findViewById(R.id.btnSave);


        imageButton.setOnClickListener(new View.OnClickListener() {   //when back btn is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddActivity.this, MyPublications.class);
                i.putExtra("emailFromAddActivityToMyPublications", emailFromMyPublicationsToAddActivity);   //send email back to MyPublications when back btn is clicked
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        Map<String, Object> map = new HashMap<>();      //create map to insert
        map.put("name", firstNameFromMyPublications);       //insert
        map.put("type", type.getText().toString());             //insert
        map.put("location", location.getText().toString());         //insert
        map.put("description", description.getText().toString());       //insert

        // Set query DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("jobs");

        query.addListenerForSingleValueEvent(new ValueEventListener() {         //search in jobs Table
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int maxId = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {              //find max publication is
                    Integer idValue = snapshot.child("publicationId").getValue(Integer.class);
                    if (idValue != null) {
                        int id = idValue.intValue();
                        if (id > maxId) {
                            maxId = id;
                        }
                    }
                }
                map.put("publicationId", maxId + 1);                    //insert (max_id + 1) in my new publication
                // maxId will contain the maximum ID value


                database.getReference("jobs").push().setValue(map);      // Push the new publication to the "jobs" node

                // Move the UI update code to the main thread using a Handler
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        location.getText().clear();     //clear edittext for next insert
                        description.getText().clear();  //clear edittext for next insert
                        type.getText().clear();         //clear edittext for next insert

                        Toast.makeText(AddActivity.this, "Congratulations! You have Just Added a New Publication!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(AddActivity.this, MyPublications.class);
                        i.putExtra("emailFromAddActivityToMyPublications", emailFromMyPublicationsToAddActivity);   //send email back to MyPublications when the back button is clicked
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }



}