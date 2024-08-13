package com.example.navbotdialog;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EventViewHolder extends RecyclerView.ViewHolder {

    ImageView eventImg;
    TextView eventName, eventDescription;

    FloatingActionButton edit,delete;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        eventImg=(ImageView) itemView.findViewById(R.id.eventImage);
        eventName = (TextView) itemView.findViewById(R.id.eventname);
        eventDescription = (TextView) itemView.findViewById(R.id.eventDesc);
        edit=itemView.findViewById(R.id.updateEvent);
        delete=itemView.findViewById(R.id.deleteEvent);

    }
}
