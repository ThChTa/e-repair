package com.example.my2ndapp;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.annotation.NonNull;
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


public class AdminDataAdapter extends FirebaseRecyclerAdapter <RecyclerViewData,AdminDataAdapter.myViewHolder> {

    private String sendToDataAdapter;  //data from Publications (full name of the admin who wants to request)
    String fn,ln;  //split string to set them in the table 'requests'
    Long pId, pId1;



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminDataAdapter(@NonNull FirebaseRecyclerOptions<RecyclerViewData> options, String sendToDataAdapter) {
        super(options);
        this.sendToDataAdapter = sendToDataAdapter;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RecyclerViewData model) {
        holder.textlocation.setText(model.getLocation());
        holder.textname.setText(model.getName() + " " + model.getLastName());
        holder.texttype.setText(model.getType());
        holder.textdescription.setText(model.getDescription());
        holder.textid.setText(String.valueOf(model.getPublicationId()));

        Log.d("textdescription", "textdescription : " + model.getDescription());
        Log.d("textid", "textid : " + model.getPublicationId());

        String itemKey = getRef(holder.getBindingAdapterPosition()).getKey(); // Use holder.getBindingAdapterPosition() instead of position
        FirebaseDatabase database = FirebaseDatabase.getInstance();



        //REQUESTS BUTTON
        holder.btnRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.textname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.request_popup))
                        .setExpanded(true,1100)
                        .create();

                dialogPlus.show();

                View view = dialogPlus.getHolderView();

                EditText txtAmount = view.findViewById(R.id.txtAmount);
                EditText txtDateAndTime = view.findViewById(R.id.txtDateAndTime);
                EditText txtMoreInfo = view.findViewById(R.id.txtMoreInfo);

                Button btnSendRequest = view.findViewById(R.id.btnSendRequest);


                DatabaseReference query1 = database.getReference("jobs").child(itemKey).child("publicationId");

                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long pId1 = dataSnapshot.getValue(Long.class);

                        if (pId1 != null) {
                            Log.d("pId1", "pId1: " + pId1);

                            Query query1 = database.getReference("requests").orderByChild("pId").equalTo(pId1);
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                                        String editTextAmount = (String) jobSnapshot.child("amount").getValue();
                                        String editTextDateAndTime = (String) jobSnapshot.child("date_and_time").getValue();
                                        String editTextMoreInfo = (String) jobSnapshot.child("more_info").getValue();
                                        txtAmount.setText(editTextAmount);
                                        txtDateAndTime.setText(editTextDateAndTime);
                                        txtMoreInfo.setText(editTextMoreInfo);
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





                btnSendRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        holder.btnRequest.setText("Sent");
                        holder.btnRequest.setBackgroundColor(Color.RED);


                        String[] arr = sendToDataAdapter.split(" ");  //split full name

                        StringBuilder fnBuilder = new StringBuilder();   //class in the Java API that provides a mutable sequence of characters

                        for (int i = 0; i < (arr.length - 1); i++) {     // we want spaces only if there is more than one first name
                            fnBuilder.append(arr[i]);
                            if (i < (arr.length - 2)) {
                                fnBuilder.append(" ");
                            }
                        }

                        fn = fnBuilder.toString();                      //init fn
                        ln = arr[arr.length - 1];                       //init ln




                        //FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference query = database.getReference("jobs").child(itemKey).child("publicationId");

                        // Read the data from the database
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                pId = dataSnapshot.getValue(Long.class);

                                if (pId != null) {
                                    Log.d("pId", "pId: " + pId);   //pId visible here only


                                    Map<String,Object> map = new HashMap<>();    // open 'gate' to start inserting data to 'requests' table
                                    map.put("amount", txtAmount.getText().toString());
                                    map.put("date_and_time", txtDateAndTime.getText().toString());
                                    map.put("more_info", txtMoreInfo.getText().toString());
                                    map.put("offer_countoffer", "offer_sent");
                                    map.put("pfn", model.getName());
                                    map.put("pln", model.getLastName());
                                    map.put("pId", pId);
                                    map.put("rfn", fn);
                                    map.put("rln", ln);
                                    map.put("rid", 9);


                                    FirebaseDatabase.getInstance().getReference().child("requests").child(itemKey).updateChildren(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialogPlus.dismiss(); // Close the dialog upon successful update
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










                    }
                });

            }




               /* @Override
                public void onClick(View v) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference query = database.getReference("jobs").child(getRef(holder.getAdapterPosition()).getKey()).child("requests");

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getValue() instanceof Long) {
                                Long requests = dataSnapshot.getValue(Long.class);
                                if (requests != null) {
                                    long newNumOfRequests = requests + 1;
                                    query.setValue(newNumOfRequests);

                                    // Check the value and hide the button if requests are 0
                                    if (newNumOfRequests == 0) {
                                        holder.btnRequests.setVisibility(View.GONE);
                                    } else {
                                        holder.btnRequests.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                // Handle the case where the "requests" data is not valid.
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any errors
                        }
                    });
                }*/
        });







    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_recycler_view_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView textlocation, textname, texttype, textdescription, textid;

        Button btnRequest;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textlocation = (TextView)itemView.findViewById(R.id.location);
            textname = (TextView)itemView.findViewById(R.id.name);
            texttype = (TextView)itemView.findViewById(R.id.type);
            textdescription = (TextView)itemView.findViewById(R.id.description);
            textid = (TextView)itemView.findViewById(R.id.publicationId);

            btnRequest = (Button)itemView.findViewById(R.id.requestButton);


        }
    }

}
