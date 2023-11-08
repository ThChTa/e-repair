package com.example.my2ndapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


    private String emailFromMyPublications;  //data from MyPublications (full name of the publication creator)
    Long pId;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DataAdapterRequests(@NonNull FirebaseRecyclerOptions<RecyclerViewDataRequests> options, String emailFromMyPublications) {
        super(options);
       this.emailFromMyPublications = emailFromMyPublications;
        }



    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RecyclerViewDataRequests model) {
        holder.textRequestName.setText("Requested by: \n" + model.getRfn() + " " + model.getRln());
        holder.textDateAndTime.setText(model.getDate_and_time());
        holder.textAmount.setText(model.getAmount());
        holder.textMoreInfo.setText(model.getMore_info());
        holder.textPId.setText(String.valueOf(model.getpId()));

        String itemKey = getRef(holder.getBindingAdapterPosition()).getKey(); // Use holder.getBindingAdapterPosition() instead of position
        FirebaseDatabase database = FirebaseDatabase.getInstance();



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");


        Query query = databaseReference.orderByChild("offer_countoffer");    //WE USE THIS QUERY TO SET/CHANGE THE COLOR AND THE TEXT OF THE REQUEST BUTTON IF THE HAVE REQUEST FROM ADMIN OR NOT

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String offerStatus = dataSnapshot.child(itemKey).child("offer_countoffer").getValue(String.class);
                if (("counteroffer_sent").equals(offerStatus)) {                                                                    //if YES
                    holder.buttonCounteroffer.setVisibility(View.VISIBLE);
                    holder.buttonCounteroffer.setText("Counteroffer Sent");
                    holder.buttonCounteroffer.setBackgroundResource(R.drawable.custom_button_for_my_publications_requests);
                } else {
                    holder.buttonCounteroffer.setVisibility(View.VISIBLE);
                    holder.buttonCounteroffer.setText("Send a counteroffer");
                    holder.buttonCounteroffer.setBackgroundResource(R.drawable.custom_button_for_publications_counteroffers);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });



        //ACCEPT BUTTON

        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.textPId.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("If you proceed you accept this request!");

                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String itemKey = getRef(holder.getBindingAdapterPosition()).getKey(); // Assuming you have the item key

                        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("requests");

                        requestsRef.child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {                                                                                //get pId from table requests from thanks to itemKey
                                    Long pId = dataSnapshot.child("pId").getValue(Long.class);
                                    if (pId != null) {


                                        // Get a reference to the "jobs" table in the Firebase database
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("jobs");      //Remove the "job" with publicationId=pId


                                        // Create a Query
                                        Query query = databaseReference.orderByChild("publicationId").equalTo(pId);


                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    // Delete the data associated with this snapshot
                                                    snapshot.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Handle any errors that occur during the delete operation
                                            }
                                        });

                                        DatabaseReference sourceTableRef = FirebaseDatabase.getInstance().getReference("requests");
                                        DatabaseReference destinationTableRef = FirebaseDatabase.getInstance().getReference("acceptTable");


                                        // Query the data from the source table using the specific key
                                        sourceTableRef.child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    // Get the data associated with the specific key
                                                    Object data = dataSnapshot.getValue();

                                                    // Write the data to the destination table with the same key
                                                    destinationTableRef.child(itemKey).setValue(data);


                                                    Map<String, Object> map = new HashMap<>();    // open 'gate' to start inserting data to 'acceptTable' table
                                                    map.put("remail",emailFromMyPublications);

                                                    FirebaseDatabase.getInstance().getReference().child("acceptTable").child(itemKey).updateChildren(map)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                }
                                                            });

                                                } else {
                                                    // Handle the case where the specific key doesn't exist in the source table
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Handle any errors that occur during the data transfer
                                            }
                                        });


                                        // Create a Query
                                        Query query2 = sourceTableRef.orderByChild("pId").equalTo(pId);


                                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    // Delete the data associated with this snapshot
                                                    snapshot.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Handle any errors that occur during the delete operation
                                            }
                                        });



                                    } else {
                                        Log.d("pId", "pId is null");
                                    }
                                } else {
                                    Log.d("pId", "Data does not exist for the given key");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle any errors that may occur
                            }
                        });


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });





        holder.buttonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.textPId.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("If you proceed you decline this request!");

                String itemKey = getRef(holder.getBindingAdapterPosition()).getKey(); // Assuming you have the item key


                builder.setPositiveButton("Decline request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("requests").child(itemKey).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });









        //REQUESTS BUTTON
        holder.buttonCounteroffer.setOnClickListener(new View.OnClickListener() {                               //when press button btnRequest

            @Override
            public void onClick(View v) {
                Context context = v.getContext(); // Get the context from the clicked view

                final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.counteroffer_popup))
                        .setExpanded(true, 1200)
                        .create();

                dialogPlus.show();

                View view = dialogPlus.getHolderView();

                EditText txtAmount = view.findViewById(R.id.txtAmount);
                EditText txtDateAndTime = view.findViewById(R.id.txtDateAndTime);
                EditText txtMoreInfo = view.findViewById(R.id.txtMoreInfo);

                Button buttonSendRequest = view.findViewById(R.id.btnSendRequest);

                Log.d("itemKey", "itemKey: " + itemKey);


                DatabaseReference query0 = database.getReference("requests").child(itemKey).child("pId");    //find publicationIds and set text to the EditTexts considering the itemkey

                query0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pId = dataSnapshot.getValue(Long.class);

                        if (pId != null) {
                            Log.d("pId", "pId: " + pId);

                            Query query1 = database.getReference("requests").orderByChild("pId").equalTo(pId);   //extract all data with the same pId
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String offerStatus = dataSnapshot.child(itemKey).child("offer_countoffer").getValue(String.class);
                                    Log.d("offerStatus", "offerStatus: " + offerStatus);
                                    Log.d("itemKey2","itemKey2" + itemKey);

                                    for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                                        String key = jobSnapshot.getKey();
                                        if (key.equals(itemKey)) {
                                            String editTextAmount = (String) jobSnapshot.child("amount").getValue();
                                            String editTextDateAndTime = (String) jobSnapshot.child("date_and_time").getValue();
                                            String editTextMoreInfo = (String) jobSnapshot.child("more_info").getValue();

                                                txtAmount.setText(editTextAmount);
                                                txtDateAndTime.setText(editTextDateAndTime);
                                                txtMoreInfo.setText(editTextMoreInfo);

                                            }
                                        }


                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage());
                                }
                            });
                        } else {
                            Log.d("pId", "publicationId is null");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors here
                    }
                });





                buttonSendRequest.setOnClickListener(new View.OnClickListener() {                  //when press Button btnSendRequest
                    @Override
                    public void onClick(View v) {

                        Log.d("Check", "checked!");   //pId visible here only
                        Log.d("emailFromMyPublicationsOnClick", emailFromMyPublications);   //pId visible here only
                        Log.d("pId", "pid mesa sto onclick: " + pId);   //pId visible here only




                        DatabaseReference query2 = database.getReference("jobs").child(itemKey).child("publicationId");  //set data to firebase onClick, to requests "table"

                        // Read the data from the database
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if (pId != null  && !TextUtils.isEmpty(txtAmount.getText().toString()) &&!TextUtils.isEmpty(txtDateAndTime.getText().toString())) {
                                    //  Log.d("pId", "pId: " + pId);   //pId visible here only

                                    Map<String, Object> map = new HashMap<>();    // open 'gate' to start inserting data to 'requests' table
                                    map.put("amount", txtAmount.getText().toString());
                                    map.put("date_and_time", txtDateAndTime.getText().toString());
                                    map.put("more_info", txtMoreInfo.getText().toString());
                                    map.put("offer_countoffer", "counteroffer_sent");


                                    // Set query DB
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference query3 = database.getReference("requests");

                                    query3.addListenerForSingleValueEvent(new ValueEventListener() {         //search in requests Table
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            String offerStatus = dataSnapshot.child(itemKey).child("offer_countoffer").getValue(String.class);
                                            if (("counteroffer_sent").equals(offerStatus)) {                                                                    //if YES
                                                holder.buttonCounteroffer.setVisibility(View.VISIBLE);
                                                holder.buttonCounteroffer.setText("Counteroffer Sent");
                                                holder.buttonCounteroffer.setBackgroundResource(R.drawable.custom_button_for_my_publications_requests);
                                            } else {
                                                holder.buttonCounteroffer.setVisibility(View.VISIBLE);
                                                holder.buttonCounteroffer.setText("Send a counteroffer");
                                                holder.buttonCounteroffer.setBackgroundResource(R.drawable.custom_button_for_publications_counteroffers);
                                            }


                                            FirebaseDatabase.getInstance().getReference().child("requests").child(itemKey).updateChildren(map)   //set the new Values to requests table
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("neopId", "neopId: " + pId);   //pId visible here only


                                                            dialogPlus.dismiss(); // Close the dialog upon successful update

                                                        }
                                                    });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle error
                                        }
                                    });

/*
                                }else if(TextUtils.isEmpty(txtMoreInfo.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "Enter Publication ID", Toast.LENGTH_SHORT).show();
                                    } else if (TextUtils.isEmpty(txtAmount.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
                                    } else if (TextUtils.isEmpty(txtDateAndTime.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "Enter Date and Time", Toast.LENGTH_SHORT).show();
                                    */} else {
                                        Log.d("pId3", "publicationId is null, or one or more of the edittexts are empty");
                                    }



                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle any errors here
                            }
                        });


                    }
                });

            }

        });



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

            buttonCounteroffer.setVisibility(View.GONE);

        }
    }

}
