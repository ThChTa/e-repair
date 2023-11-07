package com.example.my2ndapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;  // Correct import for RecyclerView

import com.example.AddActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminDataAdapter extends FirebaseRecyclerAdapter <RecyclerViewData,AdminDataAdapter.myViewHolder> {

    private String sendToDataAdapter;
    private String emailFromPublications; //Get email from Publications
    Context context;  //data from Publications (full name of the admin who wants to request)
    String fn,ln;  //split string to set them in the table 'requests'
    Long pId, pId1;




    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminDataAdapter(@NonNull FirebaseRecyclerOptions<RecyclerViewData> options, String sendToDataAdapter, String emailFromPublications, Context context) {
        super(options);
        this.sendToDataAdapter = sendToDataAdapter;
        this.emailFromPublications = emailFromPublications;
        this.context = context;
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");


        Query query = databaseReference.orderByChild("offer_countoffer");    //WE USE THIS QUERY TO SET/CHANGE THE COLOR AND THE TEXT OF THE REQUEST BUTTON IF THE HAVE REQUEST FROM ADMIN OR NOT

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String[] parts = emailFromPublications.split("@");
                    String username = parts[0];

                String offerStatus = dataSnapshot.child(itemKey + username).child("offer_countoffer").getValue(String.class);

                    if ((emailFromPublications+"_"+"offer_sent").equals(offerStatus)) {                                                                    //if YES
                        holder.btnRequest.setText("Offer Sent");
                        holder.btnRequest.setBackgroundResource(R.drawable.custom_button_for_my_publications_requests);
                    } else if("counteroffer_sent".equals(offerStatus)) {
                        holder.btnRequest.setText("See the Countoffer");
                        holder.btnRequest.setBackgroundResource(R.drawable.custom_button_for_publications_counteroffers);
                    }else{
                        holder.btnRequest.setText("Request an offer");
                        holder.btnRequest.setBackgroundResource(R.drawable.custom_button_for_my_publications_edit);
                    }
                }

        @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });




        //REQUESTS BUTTON
        holder.btnRequest.setOnClickListener(new View.OnClickListener() {                               //when press button btnRequest

            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.textname.getContext())        //show request_popup
                        .setContentHolder(new ViewHolder(R.layout.request_popup))
                        .setExpanded(true,1200)
                        .create();

                dialogPlus.show();

                View view = dialogPlus.getHolderView();

                EditText txtAmount = view.findViewById(R.id.txtAmount);
                EditText txtDateAndTime = view.findViewById(R.id.txtDateAndTime);
                EditText txtMoreInfo = view.findViewById(R.id.txtMoreInfo);

                Button btnSendRequest = view.findViewById(R.id.btnSendRequest);


                DatabaseReference query0 = database.getReference("jobs").child(itemKey).child("publicationId");    //find publicationIds and set text to the EditTexts considering the itemkey

                query0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         pId1 = dataSnapshot.getValue(Long.class);

                        if (pId1 != null) {
                            Log.d("pId1", "pId1: " + pId1);

                            Query query1 = database.getReference("requests").orderByChild("pId").equalTo(pId1);   //extract all data with the same pId1
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String[] parts = emailFromPublications.split("@");          //get "data" from data@mail.com
                                    String username = parts[0];

                                    String offerStatus = dataSnapshot.child(itemKey + username).child("offer_countoffer").getValue(String.class);
                                    Log.d("offerStatus", "offerStatus: " + offerStatus);
                                    Log.d("itemKey + username", itemKey + username);

                                    for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                                        String key = jobSnapshot.getKey();
                                        if (key.equals(itemKey+username)) {
                                            String editTextAmount = (String) jobSnapshot.child("amount").getValue();
                                            String editTextDateAndTime = (String) jobSnapshot.child("date_and_time").getValue();
                                            String editTextMoreInfo = (String) jobSnapshot.child("more_info").getValue();

                                            if (((emailFromPublications + "_" + "offer_sent").equals(offerStatus)) || ("counteroffer_sent").equals(offerStatus)) {
                                                txtAmount.setText(editTextAmount);
                                                txtDateAndTime.setText(editTextDateAndTime);
                                                txtMoreInfo.setText(editTextMoreInfo);
                                            } else {
                                                txtAmount.setText("");
                                                txtDateAndTime.setText("");
                                                txtMoreInfo.setText("");
                                                Log.d("LATHOS", "LATHOS");
                                            }
                                        }
                                    }

                                }
                                    @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage());
                                }
                            });
                        } else {
                            Log.d("pId1", "publicationId is null");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors here
                    }
                });





                btnSendRequest.setOnClickListener(new View.OnClickListener() {                  //when press Button btnSendRequest
                    @Override
                    public void onClick(View v) {

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



                        DatabaseReference query2 = database.getReference("jobs").child(itemKey).child("publicationId");  //set data to firebase onClick, to requests "table"

                        // Read the data from the database
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                pId = dataSnapshot.getValue(Long.class);

                                if (pId != null  && !TextUtils.isEmpty(txtAmount.getText().toString()) &&!TextUtils.isEmpty(txtDateAndTime.getText().toString())) {
                                  //  Log.d("pId", "pId: " + pId);   //pId visible here only

                                    Map<String, Object> map = new HashMap<>();    // open 'gate' to start inserting data to 'requests' table
                                    map.put("amount", txtAmount.getText().toString());
                                    map.put("date_and_time", txtDateAndTime.getText().toString());
                                    map.put("more_info", txtMoreInfo.getText().toString());
                                    map.put("offer_countoffer", emailFromPublications+"_"+"offer_sent");
                                    map.put("pfn", model.getName());
                                    map.put("pln", model.getLastName());
                                    map.put("pId", pId);
                                    map.put("rfn", fn);
                                    map.put("rln", ln);



                                    // Set query DB
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference queryyy = database.getReference("requests");

                                    queryyy.addListenerForSingleValueEvent(new ValueEventListener() {         //search in requests Table
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int maxRId = 0;

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {              //find max rId
                                                Integer idValue = snapshot.child("rid").getValue(Integer.class);
                                                if (idValue != null) {
                                                    int id = idValue.intValue();
                                                    if (id > maxRId) {
                                                        maxRId = id;
                                                    }
                                                }
                                            }

                                            int temp;
                                            temp = maxRId+1;


                                            map.put("rid", temp);                    //insert (rId + 1) in my new publication
                                            // maxRId will contain the maximum ID value


                                            String[] parts = emailFromPublications.split("@");
                                            String username = parts[0];

                                            FirebaseDatabase.getInstance().getReference().child("requests").child(itemKey+username).updateChildren(map)   //set new key to requests : (the_previous_key + mail)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("neo", "neo: " + pId);   //pId visible here only


                                                            // Get a reference to Firebase database
                                                            DatabaseReference databaseReferenceCountData = database.getReference("requests");    //CODE FOR NUMBER OF REQUESTS IN JOBS TABLE THANKS TO pId in requests table. The message and the button color in admins depends on num of requests

                                                            // Query and count data with pId equal to pId
                                                            databaseReferenceCountData.orderByChild("pId").equalTo(pId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    long count = dataSnapshot.getChildrenCount();

                                                                        DatabaseReference jobsRef = database.getReference("jobs"); // Reference to the "jobs" node
                                                                        Query query3 = jobsRef.orderByChild("publicationId").equalTo(pId); // Query for a specific "publicationId" value

                                                                        query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                                                                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {

                                                                                    snapshot1.getRef().child("requests").setValue(count);
                                                                                }
                                                                            }
                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage());
                                                                            }
                                                                        });


                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    Log.e("Data Count", "Error counting data: " + databaseError.getMessage());
                                                                }
                                                            });


                                                            /* CODE FOR jobs TABLE, requests values!!!!!!!!!!!!!!!!!!!!!

                                                         DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("requests");
                                                            Query queryy = dbr.orderByChild("offer_countoffer");    //WE USE THIS QUERY TO SET/CHANGE THE COLOR AND THE TEXT OF THE REQUEST BUTTON IF THE HAVE REQUEST FROM ADMIN OR NOT

                                                            queryy.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshott) {


                                                                    DatabaseReference jobsRef = database.getReference("jobs"); // Reference to the "jobs" node
                                                                    Query query3 = jobsRef.orderByChild("publicationId").equalTo(pId); // Query for a specific "publicationId" value

                                                                    query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {


                                                                            String offerStatus = dataSnapshott.child(itemKey + username).child("offer_countoffer").getValue(String.class);

                                                                            for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                                                                Long z = snapshot1.child("requests").getValue(Long.class);

                                                                                if (z != null) {
                                                                                    if ((holder.btnRequest.getText().toString().equals("Request an Offer"))) {
                                                                                        // Update process: Requests Value is the same
                                                                                        snapshot1.getRef().child("requests").setValue(100);
                                                                                    } else {
                                                                                        // Add a new request: requests = requests + 1
                                                                                        snapshot1.getRef().child("requests").setValue(200);
                                                                                    }

                                                                                } else {
                                                                                    // Set a default value if 'requests' is null
                                                                                    snapshot1.getRef().child("requests").setValue(300);
                                                                                }
                                                                                Log.d("usernm", "username: " + username);  // Access z here

                                                                                Log.d("oeoe", "z: " + (z + 1));  // Access z here
                                                                                Log.d("test_test", "os: " + offerStatus);  // Access offerStatus here
                                                                                Log.d("test_test1", "emailFromPublications: " + emailFromPublications);  // Access emailFromPublications here
                                                                            }


                                                                        }


                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                            Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage());
                                                                        }
                                                                    });
                                                                }
                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    // Handle errors
                                                                }
                                                            });*/

                                                            dialogPlus.dismiss(); // Close the dialog upon successful update





                                                            query.addListenerForSingleValueEvent(new ValueEventListener() {   //WE USE THIS QUERY TO SET/CHANGE THE COLOR AND THE TEXT OF THE REQUEST BUTTON IF THE HAVE REQUEST FROM ADMIN OR NOT (AFTER SEND REQUEST)
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    String[] parts = emailFromPublications.split("@");
                                                                    String username = parts[0];

                                                                    String offerStatus = dataSnapshot.child(itemKey + username).child("offer_countoffer").getValue(String.class);
                                                                    if ((emailFromPublications+"_"+"offer_sent").equals(offerStatus)) {                                                                    //if YES
                                                                        holder.btnRequest.setText("Offer Sent");
                                                                        holder.btnRequest.setBackgroundResource(R.drawable.custom_button_for_my_publications_requests);
                                                                    } else if ("counteroffer_sent".equals(offerStatus)) {
                                                                        holder.btnRequest.setText("See the Countoffer");
                                                                        holder.btnRequest.setBackgroundResource(R.drawable.custom_button_for_publications_counteroffers);
                                                                    } else {
                                                                        holder.btnRequest.setText("Request an offer");
                                                                        holder.btnRequest.setBackgroundResource(R.drawable.custom_button_for_my_publications_edit);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    // Handle errors
                                                                }
                                                            });
                                                        }
                                                    });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Handle error
                                        }
                                    });









                                }else if (TextUtils.isEmpty(txtAmount.getText().toString())) {
                                    Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show();

                                }else if (TextUtils.isEmpty(txtDateAndTime.getText().toString())) {
                                Toast.makeText(context, "Enter Date and Time", Toast.LENGTH_SHORT).show();
                            }
                                else {
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
