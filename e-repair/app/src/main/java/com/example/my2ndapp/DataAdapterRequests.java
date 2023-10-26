package com.example.my2ndapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.text.AllCapsTransformationMethod;
import androidx.recyclerview.widget.RecyclerView;  // Correct import for RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import java.util.HashMap;
import java.util.Map;

public class DataAdapterRequests extends FirebaseRecyclerAdapter <RecyclerViewDataRequests,DataAdapterRequests.myViewHolder> {


    private String sendToDataAdapter;  //data from MyPublications (full name of the publication creator)

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DataAdapterRequests(@NonNull FirebaseRecyclerOptions<RecyclerViewDataRequests> options, String sendToDataAdapter) {
        super(options);
        this.sendToDataAdapter = sendToDataAdapter;}



    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RecyclerViewDataRequests model) {
        holder.textlocation.setText(model.getLocation());
        holder.textname.setText(sendToDataAdapter);
        holder.texttype.setText(model.getType());
        holder.textdescription.setText(model.getDescription());
        holder.textid.setText(String.valueOf(model.getPublicationId()));


    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_requests,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView textlocation, textname, texttype, textdescription, textid;

        Button buttonAccept, buttonDecline, buttonCounteroffer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textlocation = (TextView)itemView.findViewById(R.id.downLocation);
            textname = (TextView)itemView.findViewById(R.id.requestName);
            texttype = (TextView)itemView.findViewById(R.id.downType);
            textdescription = (TextView)itemView.findViewById(R.id.downDescription);
            textid = (TextView)itemView.findViewById(R.id.publicationIdRequests);


            buttonAccept = (Button)itemView.findViewById(R.id.buttonAccept);
            buttonDecline = (Button)itemView.findViewById(R.id.buttonDecline);
            buttonCounteroffer = (Button)itemView.findViewById(R.id.buttonCounteroffer);

        }
    }

}
