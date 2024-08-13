package com.example.navbotdialog;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    Context context;
    ArrayList<EventModel> data;


    public EventAdapter(Context context, ArrayList<EventModel> data) {

        this.context = context;
        this.data = data;

    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_event_layout, parent, false);
        return new EventViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        holder.eventName.setText(data.get(position).getEventName());
        EventModel model = data.get(position);
        holder.eventName.setText(model.getEventName());
        holder.eventDescription.setText(model.getEventDesc());
        Glide.with(context)
                .load(model.getImgName())
                .placeholder(R.drawable.placeholder)
                .into(holder.eventImg);
        //Picasso.get().load(data.get(position).getImgName()).into(holder.eventImg);

        String key=model.getEventName();

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(context, ""+key, Toast.LENGTH_SHORT).show();
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.eventImg.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_dialog))
                        .setExpanded(true, 1400)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText title = myview.findViewById(R.id.edit1);
                final EditText desc = myview.findViewById(R.id.edit2);
                final FloatingActionButton img=myview.findViewById(R.id.updateImg);
                Button submit = myview.findViewById(R.id.usubmit);

                title.setText(model.getEventName());
                desc.setText(model.getEventDesc());

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    }
                });
                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("e_title", title.getText().toString());
                        map.put("e_description", desc.getText().toString());

                        FirebaseDatabase.getInstance().getReference("events")
                                .child(key).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.eventImg.getContext());
                builder.setTitle("Delete Event");
                builder.setMessage("Are you sure...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        data.remove(position);
                        FirebaseDatabase.getInstance().getReference().child("events")
                                .child(key).removeValue();


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
//
//public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
//
//    Context context;
//    ArrayList<EventModel> data;
//    public EventAdapter(Context context, ArrayList<EventModel> data) {
//        this.context = context;
//        this.data = data;
//    }
//    @NonNull
//    @Override
//    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_event_layout,parent,false);
//        return new EventViewHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
//
//        holder.eventName.setText(data.get(position).getEventName());
//        holder.eventDescription.setText(data.get(position).getEventDesc());
////        holder.eventImg.setImageResource(data.get(position).getImgName());
//
//        Glide.with(context)
//                .load(data.get(position).getImgName())
//                .into(holder.eventImg);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), Registration.class);
//
//                intent.putExtra("eventImg",data.get(position).getImgName());
//                intent.putExtra("eventName",data.get(position).getEventName());
//                intent.putExtra("eventDesc",data.get(position).getEventDesc());
//
//                context.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//}