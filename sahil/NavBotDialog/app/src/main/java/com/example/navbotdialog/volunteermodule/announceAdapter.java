package com.example.navbotdialog.volunteermodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navbotdialog.R;

import java.util.ArrayList;

public class announceAdapter extends RecyclerView.Adapter<announceAdapter.MyViewHolder>{
    private ArrayList<annouceModelClass> listData;
    private ArrayList<String> time;
    private Context context;

    public announceAdapter(Context context, ArrayList<annouceModelClass> list) {
        this.listData = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.show_announce_list,parent,false);

        return new MyViewHolder(viewItem);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        annouceModelClass dataObj=listData.get(position);


        holder.timeTxt.setText(dataObj.getTime());
        holder.dateTxt.setText(dataObj.getDate());
        holder.nameTxt.setText("Sanika");
        holder.msgTxt.setText(dataObj.getUserMsg());
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt,dateTxt, timeTxt,msgTxt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTxt = itemView.findViewById(R.id.timeTxtId);
            nameTxt=itemView.findViewById(R.id.nameTxtId);
            msgTxt=itemView.findViewById(R.id.msgTxtId);
            dateTxt=itemView.findViewById(R.id.dateTxtId);
        }
    }
}
