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


public class Publications extends AppCompatActivity {


    private RecyclerView recyclerView;

    String sendToDataAdapter1,sendToDataAdapter2,sendToDataAdapter; //pass data from this class to DataAdapter.class

    AdminDataAdapter dataAdapter;
    ImageButton imageButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications);

        recyclerView = findViewById(R.id.rvAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageButton = (ImageButton) findViewById(R.id.imageButtonPublications);

        Intent intent = getIntent();
        String emailFromAdmin = intent.getExtras().getString("emailFromAdmin");


        Log.d("sos1","emailFromAdmin = " + emailFromAdmin);




        imageButton.setOnClickListener(new View.OnClickListener() {   //when back btn is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Publications.this, Admin.class);
                //if(emailFromAdmin != null){
                    i.putExtra("emailFromPublications", emailFromAdmin);
               // }else{
                //    i.putExtra("emailFromPublications", emailFromAddActivity);
                //}

                startActivity(i);
            }
        });



        //set query DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("Users");  //get table


        query.orderByChild("email").equalTo(emailFromAdmin).addListenerForSingleValueEvent(new ValueEventListener() {  //from specific email find type
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Access the item with the specified id
                    String getAdminType = itemSnapshot.child("type").getValue(String.class);      //get type


                    sendToDataAdapter = (String) itemSnapshot.child("fn").getValue();//get first name to search for publications
                    sendToDataAdapter2 = (String) itemSnapshot.child("ln").getValue();//get last name to show in RV

                    sendToDataAdapter1 = sendToDataAdapter.concat(" ");  //concat for RV
                    sendToDataAdapter1 = sendToDataAdapter1.concat(sendToDataAdapter2); //concat for RV



                    //query starts (show RV data for type=getAdminType)


                    //I have retrieved sendToDataAdapter, now set up FirebaseRecyclerOptions
                    FirebaseRecyclerOptions<RecyclerViewData> options = new FirebaseRecyclerOptions.Builder<RecyclerViewData>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("type").equalTo(getAdminType), RecyclerViewData.class)
                            .build();

                    //Initialize the DataAdapter with the correct options and sendToDataAdapter
                    dataAdapter = new AdminDataAdapter(options, sendToDataAdapter1, Publications.this); //show to RV the full name,  Publications.this is to show Toast MESSAGE to AdminDataAdapter
                    recyclerView.setAdapter(dataAdapter);

                    //start listening to the adapter here
                    dataAdapter.startListening();
                    Log.d("full_name_of_admin", sendToDataAdapter1); //print for testing with tag: full_name_of_admin


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during data retrieval
            }
        });





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