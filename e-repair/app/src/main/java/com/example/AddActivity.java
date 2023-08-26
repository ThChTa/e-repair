package com.example;

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

import com.example.my2ndapp.MyPublications;
import com.example.my2ndapp.R;
import com.example.my2ndapp.SignIn;
import com.example.my2ndapp.User;
import com.google.firebase.database.FirebaseDatabase;

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

                location.getText().clear();     //clear edittext for next insert
                description.getText().clear();  //clear edittext for next insert
                type.getText().clear();         //clear edittext for next insert

                Toast.makeText(AddActivity.this, "Congratulations! You have Just Added a New Publication!", Toast.LENGTH_SHORT).show();

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