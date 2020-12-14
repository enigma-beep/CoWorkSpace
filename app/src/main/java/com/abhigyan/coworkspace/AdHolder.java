package com.abhigyan.coworkspace;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdHolder extends RecyclerView.ViewHolder {

    TextView user, type, value, status;
    Button btAccept;
    ImageView imgDone;
    public AdHolder(@NonNull View itemView) {
        super(itemView);

        this.status=itemView.findViewById(R.id.status);
        this.type=itemView.findViewById(R.id.rqstType);
        this.value=itemView.findViewById(R.id.rqstValue);
        this.user=itemView.findViewById(R.id.uName);
        this.btAccept=itemView.findViewById(R.id.btAccept);
        this.imgDone = itemView.findViewById(R.id.imgDone);
    }
}
