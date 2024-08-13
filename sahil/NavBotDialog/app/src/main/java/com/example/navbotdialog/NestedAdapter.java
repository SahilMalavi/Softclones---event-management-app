package com.example.navbotdialog;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.NestedViewHolder> {

    private List<String> mList;
    private String TeamName;

    public NestedAdapter(List<String> mList, String teamName) {
        this.mList = mList;
        this.TeamName = teamName;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_item, parent, false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        int count = mList.size() + 1;
        holder.mTv.setText(mList.get(position));//string as position and name,email
        holder.mTvCount.setText((position + 1) + ".");//prefix as 1,2,3

        String listItem = mList.get(position);
        String firstWord[] = listItem.split(" ", 2);
        String pos = firstWord[0];//get first word that is position of volunteer.

        String arr[] = listItem.split(" ", 6);
        String getEmail = arr[5];//get Email from current list
        String email = getEmail.replace(".", "_");//replace '.' with '_' as per Email node in a database, use this email to delete perticular listItem
        String volunteerEmail = email.trim();


        //flag=1 means setOnLongClickListener only to editTeam class instance
        holder.mTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Volunteer in " + TeamName);
                builder.setMessage("Want to delete-" + getEmail + " \n\nAre you sure...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mList.remove(position);
                        FirebaseDatabase.getInstance().getReference().child("Team")
                                .child(TeamName).child(pos).child(volunteerEmail).removeValue();

                        Toast.makeText(holder.mTv.getContext(), getEmail + " is deleted successfully.", Toast.LENGTH_SHORT).show();


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

                return true;
            }

        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class NestedViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv, mTvCount;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.nestedItemTv);
            mTvCount = itemView.findViewById(R.id.nestedItemTvCount);
        }
    }
}
