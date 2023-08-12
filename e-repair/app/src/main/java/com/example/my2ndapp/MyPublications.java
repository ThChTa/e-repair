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

    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publications);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //set the layout of the contents, i.e. list of repeating views in the recycler view
        tv = findViewById(R.id.textView4);

        Intent intent = getIntent();
        String test = intent.getExtras().getString("emailFromUser");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference jobsRef = database.getReference("jobs");

        FirebaseRecyclerOptions<RecyclerViewData> options = new FirebaseRecyclerOptions.Builder<RecyclerViewData>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("name").equalTo("paris"), RecyclerViewData.class)
                .build();

        // Initialize the RecyclerView and DataAdapter
        dataAdapter = new DataAdapter(options, null); // Initialize with a temporary value
        recyclerView.setAdapter(dataAdapter);

        // Query the data
        jobsRef.orderByChild("type").equalTo("t2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    sendToDataAdapter = (String) jobSnapshot.child("name").getValue();
                    Log.d("JobName", sendToDataAdapter);

                    // Now that you have retrieved sendToDataAdapter, update the DataAdapter
                    dataAdapter.updateSendToDataAdapter(sendToDataAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage());
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