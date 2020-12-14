package com.abhigyan.coworkspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class AdAdapter extends FirebaseRecyclerAdapter<Model,AdHolder>{

    int c, m, fl, fi;
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

        final Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newCabin=dataSnapshot.child("CabinInfo").child("Total").getValue().toString();
                c = Integer.parseInt(newCabin);
                String newMeeting=dataSnapshot.child("MeetingInfo").child("Total").getValue().toString();
                m = Integer.parseInt(newMeeting);
                String newflexi=dataSnapshot.child("SeatInfo").child("Flexi").child("Total").getValue().toString();
                fl = Integer.parseInt(newflexi);
                String newFixed=dataSnapshot.child("SeatInfo").child("Fixed").child("Total").getValue().toString();
                fi = Integer.parseInt(newFixed);
//                        Toast.makeText(, "Inside value listener..n="+n, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
//                        Toast.makeText(AddCabins.this, "Error", Toast.LENGTH_LONG).show();

            }
        });


        holder.btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// button click event

                String time_stamp = getRef(position).getKey();
                String uname = holder.user.getText().toString();

                String utype = holder.type.getText().toString();
                String uvalue = holder.value.getText().toString();

                ////////////////////////////////////////////////////////////////////////////////////



                int x=Integer.parseInt(uvalue);
                if(utype.equals("cabin")){
                    if(c<x){
                        Toast.makeText(v.getContext(),"Cabin request more than available cabins. Add more Cabins",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("info").child("CabinInfo").child("Total").setValue(String.valueOf(c-x));
                        FirebaseDatabase.getInstance().getReference().child("requests").child(time_stamp).child("status").setValue("true");
                        FirebaseDatabase.getInstance().getReference().child("users").child(uname).child("requests").child(time_stamp).child("status").setValue("true");
                        holder.btAccept.setVisibility(View.INVISIBLE);
                    }
                }
                else if(utype.equals("meeting")){
                    if(m<x){
                        Toast.makeText(v.getContext(),"Meeting Room request more than available Meeting rooms. Add more Rooms",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("info").child("MeetingInfo").child("Total").setValue(String.valueOf(m-x));
                        FirebaseDatabase.getInstance().getReference().child("requests").child(time_stamp).child("status").setValue("true");
                        FirebaseDatabase.getInstance().getReference().child("users").child(uname).child("requests").child(time_stamp).child("status").setValue("true");
                        holder.btAccept.setVisibility(View.INVISIBLE);
                    }
                }
                else if(utype.equals("Flexi")){
                    if(fl<x){
                        Toast.makeText(v.getContext(),"Flexi Seat request more than available Flexi Seat. Add more Flexi Seats",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("info").child("SeatInfo").child("Flexi").child("Total").setValue(String.valueOf(fl-x));
                        FirebaseDatabase.getInstance().getReference().child("requests").child(time_stamp).child("status").setValue("true");
                        FirebaseDatabase.getInstance().getReference().child("users").child(uname).child("requests").child(time_stamp).child("status").setValue("true");
                        holder.btAccept.setVisibility(View.INVISIBLE);
                    }
                }
                else if(utype.equals("Fixed")){
                    if(fi<x){
                        Toast.makeText(v.getContext(),"Fixed Seat request more than available Fixed Seat. Add more Fixed Seats",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("info").child("SeatInfo").child("Fixed").child("Total").setValue(String.valueOf(fi-x));
                        FirebaseDatabase.getInstance().getReference().child("requests").child(time_stamp).child("status").setValue("true");
                        FirebaseDatabase.getInstance().getReference().child("users").child(uname).child("requests").child(time_stamp).child("status").setValue("true");
                        holder.btAccept.setVisibility(View.INVISIBLE);
                    }
                }


                ////////////////////////////////////////////////////////////////////////////////////

                /*

               *****************WORKING CODE************************
                if(utype.equals("cabin")){
                    FirebaseDatabase.getInstance().getReference().child("info").child("CabinInfo").child("Total").setValue(String.valueOf(c-x));
                }
                else if(utype.equals("meeting")){
                    FirebaseDatabase.getInstance().getReference().child("info").child("MeetingInfo").child("Total").setValue(String.valueOf(m-x));
                }
                else if(utype.equals("Flexi")){
                    FirebaseDatabase.getInstance().getReference().child("info").child("SeatInfo").child("Flexi").child("Total").setValue(String.valueOf(fl-x));
                }
                else if(utype.equals("Fixed")){
                    FirebaseDatabase.getInstance().getReference().child("info").child("SeatInfo").child("Fixed").child("Total").setValue(String.valueOf(fi-x));
                }
//                holder.status.setText(uname);

                 */

                ////////////////////////////////////////////////////////////////////////////////////
//                FirebaseDatabase.getInstance().getReference().child("requests").child(time_stamp).child("status").setValue("true");
//                FirebaseDatabase.getInstance().getReference().child("users").child(uname).child("requests").child(time_stamp).child("status").setValue("true");
//                holder.btAccept.setVisibility(View.INVISIBLE);

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