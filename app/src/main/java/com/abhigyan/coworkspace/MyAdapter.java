package com.abhigyan.coworkspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



public class MyAdapter extends FirebaseRecyclerAdapter<Model,MyHolder>{
    public MyAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull Model model) {
        holder.user.setText(model.getUser());
        holder.type.setText(model.getType());
        holder.value.setText(model.getValue());
        holder.status.setText(model.getStatus());

        if(holder.status.getText().equals("true")){
            holder.imgReject.setVisibility(View.INVISIBLE);
            holder.imgAccept.setVisibility(View.VISIBLE);
        }
        else{
            holder.imgReject.setVisibility(View.VISIBLE);
            holder.imgAccept.setVisibility(View.INVISIBLE);
        }

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new MyHolder(view);
    }


//    class myviewholder extends RecyclerView.ViewHolder{
//
//        TextView user,type,value,status;
//
//        public myviewholder(@NonNull View itemView) {
//            super(itemView);
//            user=itemView.findViewById(R.id.uName);
//            type=itemView.findViewById(R.id.rqstType);
//            value=itemView.findViewById(R.id.rqstValue);
//            status=itemView.findViewById(R.id.status);
//
//        }
//    }
}