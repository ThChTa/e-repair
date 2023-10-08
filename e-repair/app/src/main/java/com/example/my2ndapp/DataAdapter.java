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
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import java.util.HashMap;
import java.util.Map;

public class DataAdapter extends FirebaseRecyclerAdapter <RecyclerViewData,DataAdapter.myViewHolder> {


    private String sendToDataAdapter;  //data from MyPublications

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

                        String itemKey = getRef(holder.getAdapterPosition()).getKey(); // Use holder.getAdapterPosition() instead of position

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

        holder.btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.textname.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted Data cannot be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("jobs").child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
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
