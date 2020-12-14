package com.abhigyan.coworkspace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class AdAdapter extends FirebaseRecyclerAdapter<Model,AdHolder>{
    public AdAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final AdHolder holder, final int position, @NonNull Model model) {
        holder.user.setText(model.getUser());
        holder.type.setText(model.getType());
        holder.value.setText(model.getValue());
        holder.status.setText(model.getStatus());

        if(holder.status.getText().equals("true")){
            holder.btAccept.setVisibility(View.INVISIBLE);
            holder.imgDone.setVisibility(View.VISIBLE);
        }
        else{
            holder.btAccept.setVisibility(View.VISIBLE);
            holder.imgDone.setVisibility(View.INVISIBLE);
        }


        holder.btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// button click event

                String time_stamp = getRef(position).getKey();
                String uname = holder.user.getText().toString();

                String utype = holder.type.getText().toString();
                String uvalue = holder.value.getText().toString();

//                int x=Integer.parseInt(uvalue);
//                if(utype.equals("cabin")){
//                    String old= FirebaseDatabase.getInstance().getReference().child("info").child("CabinInfo").child("Total").setValue();
//                }
//                holder.status.setText(uname);
                FirebaseDatabase.getInstance().getReference().child("requests").child(time_stamp).child("status").setValue("true");
                FirebaseDatabase.getInstance().getReference().child("users").child(uname).child("requests").child(time_stamp).child("status").setValue("true");
                holder.btAccept.setVisibility(View.INVISIBLE);

            }

        });
    }

    @NonNull
    @Override
    public AdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2,parent,false);
        return new AdHolder(view);
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