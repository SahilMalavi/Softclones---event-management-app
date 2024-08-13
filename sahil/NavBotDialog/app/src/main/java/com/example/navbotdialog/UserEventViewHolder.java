package com.example.navbotdialog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserEventViewHolder extends RecyclerView.ViewHolder {

    ImageView eventImg;
    TextView eventName, eventDescription;

    public UserEventViewHolder(@NonNull View itemView) {
        super(itemView);
        eventImg=(ImageView) itemView.findViewById(R.id.eventImage);
        eventName = (TextView) itemView.findViewById(R.id.eventname);
        eventDescription = (TextView) itemView.findViewById(R.id.eventDesc);
    }
}
