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
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    String sendToDataAdapter, sendToDataAdapter2; //pass data from this class to DataAdapter.class


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publications);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String test = intent.getExtras().getString("emailFromUser");

        //set query DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("Users");

        //query starts

        query.orderByChild("email").equalTo(test).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    sendToDataAdapter = (String) jobSnapshot.child("fn").getValue();//get first name
                    Log.d("name", sendToDataAdapter); //print for testing with tag: name

                    //I have retrieved sendToDataAdapter, now set up FirebaseRecyclerOptions
                    FirebaseRecyclerOptions<RecyclerViewData> options = new FirebaseRecyclerOptions.Builder<RecyclerViewData>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("name").equalTo(sendToDataAdapter), RecyclerViewData.class)
                            .build();

                    //Initialize the DataAdapter with the correct options and sendToDataAdapter
                    dataAdapter = new DataAdapter(options, sendToDataAdapter);
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