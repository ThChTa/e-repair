package com.example.my2ndapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;



public class MyPublications extends AppCompatActivity {

    private RecyclerView recyclerView;

    DataAdapter dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publications);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //set the layout of the contents, i.e. list of repeating views in the recycler view

        FirebaseRecyclerOptions<RecyclerViewData> options = new FirebaseRecyclerOptions.Builder<RecyclerViewData>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("jobs"), RecyclerViewData.class)
                .build();

        dataAdapter = new DataAdapter(options);
        recyclerView.setAdapter(dataAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        dataAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataAdapter.stopListening();
    }
}