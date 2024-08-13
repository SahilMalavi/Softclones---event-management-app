package com.example.navbotdialog.volunteermodule;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navbotdialog.R;

import java.util.List;

public class NestedAdapter_Team extends RecyclerView.Adapter<NestedAdapter_Team.NestedViewHolder_Team> {

    private List<String> mList;
    private String TeamName;

    private ArrayAdapter<String> adapter;
    public NestedAdapter_Team(List<String> mList, String teamName) {
        this.mList = mList;
        this.TeamName = teamName;}

    @NonNull
    @Override
    public NestedViewHolder_Team onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_item_team_volunteer, parent, false);
        return new NestedViewHolder_Team(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder_Team holder, @SuppressLint("RecyclerView") int position) {
//        int count = mList.size() + 1;
        holder.mTv.setText(mList.get(position));//string as position,name and email
        holder.mTvCount.setText((position + 1) + ".");//prefix as 1,2,3


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder_Team extends RecyclerView.ViewHolder {
        private TextView mTv, mTvCount,AddTaskTv;

        public NestedViewHolder_Team(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.nestedItemTv_team);
            mTvCount = itemView.findViewById(R.id.nestedItemTvCount_team);
            AddTaskTv = itemView.findViewById(R.id.Assign_task);
        }
    }
}
