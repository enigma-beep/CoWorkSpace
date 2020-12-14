package com.abhigyan.coworkspace;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {

    TextView user, type, value, status;
    ImageView imgAccept, imgReject;
    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.status=itemView.findViewById(R.id.status);
        this.type=itemView.findViewById(R.id.rqstType);
        this.value=itemView.findViewById(R.id.rqstValue);
        this.user=itemView.findViewById(R.id.uName);
        this.imgAccept = itemView.findViewById(R.id.imgAccept);
        this.imgReject = itemView.findViewById(R.id.imgReject);
    }
}
