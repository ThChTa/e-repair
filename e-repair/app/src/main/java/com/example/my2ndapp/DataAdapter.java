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

public class DataAdapter extends FirebaseRecyclerAdapter <RecyclerViewData,DataAdapter.myViewHolder> {


    private String sendToDataAdapter;  //data from MyPublications (full name of the publication creator)
    private String emailFromMyPublications; //Get email from MyPublications
    private Long pIdFromMyPublications;  // Get pId from MyPublications and send it to Requests_Page

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public DataAdapter(@NonNull FirebaseRecyclerOptions<RecyclerViewData> options, String sendToDataAdapter, String emailFromMyPublications) {
        super(options);
        this.sendToDataAdapter = sendToDataAdapter;
        this.emailFromMyPublications = emailFromMyPublications;  //access the email of the user to use it in putExtras and getExtras (for example for switch between MyPublications and Request_Page
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RecyclerViewData model) {
        holder.textlocation.setText(model.getLocation());
        holder.textname.setText(sendToDataAdapter);
        holder.texttype.setText(model.getType());
        holder.textdescription.setText(model.getDescription());
        holder.textid.setText(String.valueOf(model.getPublicationId()));


        //EDIT BUTTON

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.textname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1200)
                        .create();

                //dialogPlus.show();

                View view = dialogPlus.getHolderView();

                EditText newtype = view.findViewById(R.id.txtType);
                EditText newlocation = view.findViewById(R.id.txtLocation);
                EditText newdescription = view.findViewById(R.id.txtDescription);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                newtype.setText(model.getType());
                newlocation.setText(model.getLocation());
                newdescription.setText(model.getDescription());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("type", newtype.getText().toString());
                        map.put("location", newlocation.getText().toString());
                        map.put("description", newdescription.getText().toString());

                        String itemKey = getRef(holder.getBindingAdapterPosition()).getKey(); // Use holder.getAdapterPosition() instead of position

                        FirebaseDatabase.getInstance().getReference().child("jobs").child(itemKey).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss(); // Close the dialog upon successful update
                                    }
                                });
                    }
                });

            }
        });

        //DELETE BUTTON

        holder.btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.textname.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted Data cannot be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("jobs").child(getRef(holder.getBindingAdapterPosition()).getKey()).removeValue();
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

        holder.btnRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemKey = getRef(holder.getBindingAdapterPosition()).getKey(); // Use holder.getBindingAdapterPosition() instead of position
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("jobs");


                Query query = databaseReference.orderByChild("publicationId");    //WE USE THIS QUERY TO SET/CHANGE THE COLOR AND THE TEXT OF THE REQUEST BUTTON IF THE HAVE REQUEST FROM ADMIN OR NOT

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        pIdFromMyPublications = dataSnapshot.child(itemKey).child("publicationId").getValue(Long.class);
                        Intent intent = new Intent(holder.textname.getContext(), Requests_Page.class);
                        intent.putExtra("pIdFromMyPublications", pIdFromMyPublications);         //send pId to the Request_Page
                        intent.putExtra("emailFromMyPublications", emailFromMyPublications);   //send email to the Request_Page
                        // Start the new activity
                        holder.textname.getContext().startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });


            }
        });



        /*

            //REQUESTS BUTTON
            holder.btnRequests.setOnClickListener(new View.OnClickListener() {




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


                    DatabaseReference query0 = database.getReference("jobs").child(itemKey).child("publicationId");    //find publicationIds and set text to the EditTexts considering the itemkey

                    query0.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pId1 = dataSnapshot.getValue(Long.class);

                            if (pId1 != null) {
                                Log.d("pId1", "pId1: " + pId1);

                                Query query0 = database.getReference("requests").orderByChild("pId").equalTo(pId1);
                                query0.addValueEventListener(new ValueEventListener() {
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



                    DatabaseReference query1 = database.getReference("jobs").child(getRef(holder.getBindingAdapterPosition()).getKey()).child("publicationId");

                        // Read the data from the database
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    pId = dataSnapshot.getValue(Long.class);

                                    if (pId != null) {
                                        Log.d("pId", "pId: " + pId);   //pId visible here only


                                        Map<String,Object> map = new HashMap<>();    // open 'gate' to start inserting data to 'requests' table
                                        map.put("amount", txtAmount.getText().toString());
                                        map.put("date_and_time", txtDateAndTime.getText().toString());
                                        map.put("more_info", txtMoreInfo.getText().toString());
                                        map.put("offer_countoffer", "countoffer_sent");
                                        map.put("pId", pId);


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

            });*/

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        Query query = databaseReference.orderByChild("offer_countoffer").equalTo("offer_sent");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String offerStatus = dataSnapshot.child(getRef(holder.getAdapterPosition()).getKey()).child("offer_countoffer").getValue(String.class);


                        // Check the value and hide the button if requests are 0
                    if ("offer_sent".equals(offerStatus)) {
                            holder.btnRequests.setVisibility(View.VISIBLE);
                        } else {
                            holder.btnRequests.setVisibility(View.GONE);
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

        // Set the visibility of the button initially
        if (model.getRequests() == 0) {
            holder.btnRequests.setVisibility(View.GONE);
        } else {
            holder.btnRequests.setVisibility(View.VISIBLE);
        }
        }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView textlocation, textname, texttype, textdescription, textid;

        Button btnEdit, btnDelete, btnRequests;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textlocation = (TextView)itemView.findViewById(R.id.location);
            textname = (TextView)itemView.findViewById(R.id.name);
            texttype = (TextView)itemView.findViewById(R.id.type);
            textdescription = (TextView)itemView.findViewById(R.id.description);
            textid = (TextView)itemView.findViewById(R.id.publicationId);


            btnEdit = (Button)itemView.findViewById(R.id.buttonEdit);
            btnDelete = (Button)itemView.findViewById(R.id.buttonDelete);
            btnRequests = (Button)itemView.findViewById(R.id.buttonRequests);

        }
    }

}
