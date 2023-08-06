package com.example.my2ndapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class DataAdapter extends FirebaseRecyclerAdapter <RecyclerViewData,DataAdapter.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DataAdapter(@NonNull FirebaseRecyclerOptions<RecyclerViewData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RecyclerViewData model) {
        holder.textlocation.setText(model.getLocation());
        holder.textname.setText(model.getName());
        holder.texttype.setText(model.getType());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView textlocation, textname, texttype;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textlocation = (TextView)itemView.findViewById(R.id.location);
            textname = (TextView)itemView.findViewById(R.id.name);
            texttype = (TextView)itemView.findViewById(R.id.type);

        }
    }
}
