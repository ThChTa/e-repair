package com.example.my2ndapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.AddActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyPublications extends AppCompatActivity {


    private RecyclerView recyclerView;

    DataAdapter dataAdapter;
    String sendToDataAdapter1,sendToDataAdapter2,sendToDataAdapter; //pass data from this class to DataAdapter.class
    ImageButton imageButton;

    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publications);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageButton = (ImageButton) findViewById(R.id.imageButtonMyPublications);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);  //plus btn

        Intent intent = getIntent();
        String emailFromUser = intent.getExtras().getString("emailFromUser");
        String emailFromAddActivity = intent.getExtras().getString("emailFromAddActivityToMyPublications");

        Log.d("sos1","emailFromUser = " + emailFromUser + ", emailFromAddActivity = " + emailFromAddActivity);




        imageButton.setOnClickListener(new View.OnClickListener() {   //when back btn is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyPublications.this, User.class);
                if(emailFromUser != null){
                    i.putExtra("emailFromMyPublications", emailFromUser);
                }else{
                    i.putExtra("emailFromMyPublications", emailFromAddActivity);
                }

                startActivity(i);
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {       //when plus btn is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyPublications.this, AddActivity.class);
                i.putExtra("firstNameFromMyPublications", sendToDataAdapter);

                if(emailFromUser != null){
                    i.putExtra("emailFromMyPublications", emailFromUser);
                }else{
                    i.putExtra("emailFromMyPublications", emailFromAddActivity);
                }// from MyPublications to AddActivity

                startActivity(i);

            }
        });


        //set query DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("Users");

        //query starts

        if(emailFromUser != null){   //when navigate from User.class
            query.orderByChild("email").equalTo(emailFromUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                        sendToDataAdapter = (String) jobSnapshot.child("fn").getValue();//get first name to search for publications
                        sendToDataAdapter2 = (String) jobSnapshot.child("ln").getValue();//get last name to show in RV

                        sendToDataAdapter1 = sendToDataAdapter.concat(" ");  //concat for RV
                        sendToDataAdapter1 = sendToDataAdapter1.concat(sendToDataAdapter2); //concat for RV
                        Log.d("full_name", sendToDataAdapter2); //print for testing with tag: full_name

                        //I have retrieved sendToDataAdapter, now set up FirebaseRecyclerOptions
                        FirebaseRecyclerOptions<RecyclerViewData> options = new FirebaseRecyclerOptions.Builder<RecyclerViewData>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("name").equalTo(sendToDataAdapter), RecyclerViewData.class)
                                .build();

                        //Initialize the DataAdapter with the correct options and sendToDataAdapter
                        dataAdapter = new DataAdapter(options, sendToDataAdapter1); //show to RV the full name
                        recyclerView.setAdapter(dataAdapter);

                        //start listening to the adapter here
                        dataAdapter.startListening();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage()); //print if error exists
                }
            });  //query ends



        }   //if statement ends

        else {             //when navigate from AddActivity.class
            query.orderByChild("email").equalTo(emailFromAddActivity).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                        sendToDataAdapter = (String) jobSnapshot.child("fn").getValue();//get first name to search for publications
                        sendToDataAdapter2 = (String) jobSnapshot.child("ln").getValue();//get last name to show in RV

                        sendToDataAdapter1 = sendToDataAdapter.concat(" ");  //concat for RV
                        sendToDataAdapter1 = sendToDataAdapter1.concat(sendToDataAdapter2); //concat for RV
                        Log.d("full_name", sendToDataAdapter2); //print for testing with tag: full_name

                        //I have retrieved sendToDataAdapter, now set up FirebaseRecyclerOptions
                        FirebaseRecyclerOptions<RecyclerViewData> options = new FirebaseRecyclerOptions.Builder<RecyclerViewData>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("name").equalTo(sendToDataAdapter), RecyclerViewData.class)
                                .build();

                        //Initialize the DataAdapter with the correct options and sendToDataAdapter
                        dataAdapter = new DataAdapter(options, sendToDataAdapter1); //show to RV the full name
                        recyclerView.setAdapter(dataAdapter);

                        //start listening to the adapter here
                        dataAdapter.startListening();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage()); //print if error exists
                }
            });  //query ends

        }       //else ends


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (dataAdapter != null) {
            dataAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataAdapter.stopListening();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    //for action bar

        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str){
        FirebaseRecyclerOptions<RecyclerViewData> options = new FirebaseRecyclerOptions.Builder<RecyclerViewData>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("type").startAt(str).endAt(str+"~"), RecyclerViewData.class)
                .build();

        dataAdapter  = new DataAdapter(options,sendToDataAdapter);

        dataAdapter.startListening();
        recyclerView.setAdapter(dataAdapter);
    }*/

}