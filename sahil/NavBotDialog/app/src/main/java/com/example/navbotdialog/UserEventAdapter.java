package com.example.navbotdialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
public class UserEventAdapter extends RecyclerView.Adapter<UserEventViewHolder> {
    Context context;
    ArrayList<UserEventModel> data;
    public UserEventAdapter(Context context, ArrayList<UserEventModel> data) {
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public UserEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_event_layout,parent,false);
        return new UserEventViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserEventViewHolder holder, int position) {

        holder.eventName.setText(data.get(position).getEventName());
        holder.eventDescription.setText(data.get(position).getEventDesc());
//        holder.eventImg.setImageResource(data.get(position).getImgName());

        Glide.with(context)
                .load(data.get(position).getImgName())
                .into(holder.eventImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Registration.class);

                intent.putExtra("eventImg",data.get(position).getImgName());
                intent.putExtra("eventName",data.get(position).getEventName());
                intent.putExtra("eventDesc",data.get(position).getEventDesc());

                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
       return data.size();
    }
}
