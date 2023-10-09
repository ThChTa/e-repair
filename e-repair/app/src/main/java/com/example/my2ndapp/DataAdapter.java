package com.example.my2ndapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import java.util.HashMap;
import java.util.Map;

public class DataAdapter extends FirebaseRecyclerAdapter <RecyclerViewData,DataAdapter.myViewHolder> {


    private String sendToDataAdapter;  //data from MyPublications (full name of the publication creator)
    String fn,ln;  //split string to set them in the table 'requests'
    Long pId;



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DataAdapter(@NonNull FirebaseRecyclerOptions<RecyclerViewData> options, String sendToDataAdapter) {
        super(options);
        this.sendToDataAdapter = sendToDataAdapter;
    }



    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RecyclerViewData model) {
        holder.textlocation.setText(model.getLocation());
        holder.textname.setText(sendToDataAdapter);
        holder.texttype.setText(model.getType());
        holder.textdescription.setText(model.getDescription());
        holder.textid.setText(String.valueOf(model.getPublicationId()));

        Log.d("textdescription", "textdescription : " + model.getDescription());
        Log.d("textid", "textid : " + model.getPublicationId());


        //EDIT BUTTON

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.textname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1100)
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
                        // instead of FirebaseDatabase.getInstance().getReference().child("jobs").child(getRef(position).getKey()).removeValue();

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


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference query = database.getReference("jobs").child(getRef(holder.getBindingAdapterPosition()).getKey()).child("publicationId");

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
                                        map.put("pId", pId);
                                        map.put("rfn", "");
                                        map.put("rln", "");
                                        map.put("rid", 9);

                                        String itemKey = getRef(holder.getBindingAdapterPosition()).getKey(); // Use holder.getBindingAdapterPosition() instead of position

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
