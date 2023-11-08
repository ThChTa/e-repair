package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private Spinner spinner;
    EditText location,description;
    Button btnAdd;
    ImageButton imageButton;
    String firstNameFromMyPublications,lastNameFromMyPublications, emailFromMyPublicationsToAddActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        firstNameFromMyPublications = intent.getExtras().getString("firstNameFromMyPublications");  //get first name and last name from MyPublications
        lastNameFromMyPublications = intent.getExtras().getString("lastNameFromMyPublications");  //get first name and last name from MyPublications
        emailFromMyPublicationsToAddActivity = intent.getExtras().getString("emailFromMyPublications"); //get email from MyPublications to send it back when back btn is clicked

        imageButton = (ImageButton) findViewById(R.id.imageButtonAddActivity);

        spinner= (Spinner)findViewById(R.id.addActivitySpinner);
        location = (EditText)findViewById(R.id.addLocation);
        description = (EditText)findViewById(R.id.addDescription);

        btnAdd = (Button)findViewById(R.id.btnSave);




        List<String> Categories = new ArrayList<>();
        Categories.add(0,"Choose type");
        Categories.add("Mechanical Engineer");
        Categories.add("Computer Programmer");
        Categories.add("HVAC Technician: HVAC (Heating, Ventilation, and Air Conditioning)");
        Categories.add("Welder");
        Categories.add("Plasterer");
        Categories.add("Automotive Mechanic");
        Categories.add("Carpenter");
        Categories.add("Mason");
        Categories.add("Landscape Designer");
        Categories.add("Architect");
        Categories.add("Plumber");
        Categories.add("Solar Panel Installer");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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

        String category = spinner.getSelectedItem().toString();


        Map<String, Object> map = new HashMap<>();      //create map to insert
        map.put("name", firstNameFromMyPublications);       //insert
        map.put("lastName", lastNameFromMyPublications);       //insert
        map.put("pemail",emailFromMyPublicationsToAddActivity);
        map.put("type", category);             //insert
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

                int temp;
                temp = maxId+1;

                map.put("publicationId", temp);                    //insert (max_id + 1) in my new publication
                // maxId will contain the maximum ID value

                map.put("requests",0);                             //when add new publication set requests equal to 0






                database.getReference("jobs").push().setValue(map);      // Push the new publication to the "jobs" node

                // Move the UI update code to the main thread using a Handler
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        location.getText().clear();     //clear edittext for next insert
                        description.getText().clear();  //clear edittext for next insert

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