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


    //private String sendToDataAdapter;  //data from MyPublications (full name of the publication creator)

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DataAdapterRequests(@NonNull FirebaseRecyclerOptions<RecyclerViewDataRequests> options) {
        super(options);
       // this.sendToDataAdapter = sendToDataAdapter;
        }



    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RecyclerViewDataRequests model) {
        holder.textRequestName.setText("Requested by: \n" + model.getRfn() + " " + model.getRln());
        holder.textDateAndTime.setText(model.getDate_and_time());
        holder.textAmount.setText(model.getAmount());
        holder.textMoreInfo.setText(model.getMore_info());
        holder.textPId.setText(String.valueOf(model.getpId()));

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_requests,parent,false);
        return new myViewHolder(view);


    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView textRequestName, textDateAndTime, textAmount, textMoreInfo, textPId;

        Button buttonAccept, buttonDecline, buttonCounteroffer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textRequestName = (TextView)itemView.findViewById(R.id.requestName);
            textDateAndTime = (TextView)itemView.findViewById(R.id.downDateAndTime);
            textAmount = (TextView)itemView.findViewById(R.id.downAmount);
            textMoreInfo = (TextView)itemView.findViewById(R.id.downMoreInfo);
            textPId = (TextView)itemView.findViewById(R.id.publicationIdRequests);


            buttonAccept = (Button)itemView.findViewById(R.id.buttonAccept);
            buttonDecline = (Button)itemView.findViewById(R.id.buttonDecline);
            buttonCounteroffer = (Button)itemView.findViewById(R.id.buttonCounteroffer);

        }
    }

}
