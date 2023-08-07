package com.example.my2ndapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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

        dataAdapter  = new DataAdapter(options);
        dataAdapter.startListening();
        recyclerView.setAdapter(dataAdapter);
    }
}