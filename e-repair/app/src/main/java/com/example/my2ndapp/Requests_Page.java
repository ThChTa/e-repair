package com.example.my2ndapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Requests_Page extends AppCompatActivity {

    DataAdapterRequests dataAdapterRequests;

    private RecyclerView recyclerView;

    String sendToDataAdapter1,sendToDataAdapter2,sendToDataAdapter; //pass data from this class to DataAdapter.class
    ImageButton backButton;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page);

        recyclerView = findViewById(R.id.rvRequests); // Replace with the actual ID of your RecyclerView in the XML layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set a layout manager


        backButton = (ImageButton) findViewById(R.id.imageButtonRequestsPageActivity);


        Intent intent = getIntent();
        String emailFromMyPublications = intent.getExtras().getString("emailFromMyPublications");  //get email from MyPublications


        backButton.setOnClickListener(new View.OnClickListener() {   //when back btn is clicked
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Requests_Page.this, MyPublications.class);
                i.putExtra("emailFromRequestsPage", emailFromMyPublications);   //emailFromRequestsPage = emailFromMyPublications, so i have one variable
                startActivity(i);
            }
        });




        //set query DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("Users");  //get table


        query.orderByChild("email").equalTo(emailFromMyPublications).addListenerForSingleValueEvent(new ValueEventListener() {  //from specific email find type
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Access the item with the specified id
                    String getAdminType = itemSnapshot.child("type").getValue(String.class);      //get type

                    Log.d("type_of", getAdminType); //print for testing with tag: full_name_of_admin
                    sendToDataAdapter = (String) itemSnapshot.child("fn").getValue();//get first name to search for publications
                    sendToDataAdapter2 = (String) itemSnapshot.child("ln").getValue();//get last name to show in RV

                    sendToDataAdapter1 = sendToDataAdapter.concat(" ");  //concat for RV
                    sendToDataAdapter1 = sendToDataAdapter1.concat(sendToDataAdapter2); //concat for RV



                    //query starts (show RV data for type=getAdminType)


                    //I have retrieved sendToDataAdapter, now set up FirebaseRecyclerOptions
                    FirebaseRecyclerOptions<RecyclerViewDataRequests> options = new FirebaseRecyclerOptions.Builder<RecyclerViewDataRequests>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("name").equalTo("Thomas"), RecyclerViewDataRequests.class)
                            .build();

                    //Initialize the DataAdapter with the correct options and sendToDataAdapter
                    dataAdapterRequests = new DataAdapterRequests(options, sendToDataAdapter1); //show to RV the full name,  Publications.this is to show Toast MESSAGE to AdminDataAdapter
                    recyclerView.setAdapter(dataAdapterRequests);

                    //start listening to the adapter here
                    dataAdapterRequests.startListening();

                    Log.d("full_name_of", sendToDataAdapter1); //print for testing with tag: full_name_of_admin


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
        if (dataAdapterRequests != null) {
            dataAdapterRequests.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataAdapterRequests.stopListening();
    }
}


