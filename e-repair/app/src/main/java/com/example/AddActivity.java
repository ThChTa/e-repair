package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.my2ndapp.R;
import com.example.my2ndapp.SignIn;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText type,location,description;
    Button btnAdd;
    String firstNameFromMyPublications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        firstNameFromMyPublications = intent.getExtras().getString("firstNameFromMyPublications");

        type = (EditText)findViewById(R.id.addType);
        location = (EditText)findViewById(R.id.addLocation);
        description = (EditText)findViewById(R.id.addDescription);

        btnAdd = (Button)findViewById(R.id.btnSave);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();

                type.getText().clear();         //clear edittext for next insert
                location.getText().clear();     //clear edittext for next insert
                description.getText().clear();  //clear edittext for next insert

                Toast.makeText(AddActivity.this, "Congratulations!\n" +
                        "You have Just Added a New Publication!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void insertData(){

        Map<String,Object> map = new HashMap<>();
        map.put("name",firstNameFromMyPublications);
        map.put("type",type.getText().toString());
        map.put("location",location.getText().toString());
        map.put("description",description.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("jobs").push().setValue(map);

    }


}