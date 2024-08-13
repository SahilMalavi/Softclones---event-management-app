package com.example.navbotdialog;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NestedAdapter_Team extends RecyclerView.Adapter<NestedAdapter_Team.NestedViewHolder_Team> {

    private List<String> mList;
    private String TeamName;
    private ArrayList<String> tasks;
    private ArrayAdapter<String> adapter;
    public NestedAdapter_Team(List<String> mList, String teamName,ArrayList<String> Tlist) {
        this.mList = mList;
        this.TeamName = teamName;
        this.tasks = Tlist;
    }

    @NonNull
    @Override
    public NestedViewHolder_Team onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_item_team, parent, false);
        return new NestedViewHolder_Team(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder_Team holder, @SuppressLint("RecyclerView") int position) {
//        int count = mList.size() + 1;
        holder.mTv.setText(mList.get(position));//string as position and name,email
        holder.mTvCount.setText((position + 1) + ".");//prefix as 1,2,3

        String listItem = mList.get(position);
        String firstWord[] = listItem.split(" ", 2);
        String pos = firstWord[0];//get first word that is position of volunteer.

        String arr[] = listItem.split("\\|", 3);
        String mail = arr[1];//get Email from current list
        String getEmail=mail.trim();
        String volunteerEmail = getEmail.replace(".", "_").trim();//replace '.' with '_' as per Email node in a database, use this email to delete perticular listItem

        tasks = new ArrayList<>();
        adapter = new ArrayAdapter<>(holder.itemView.getContext(), android.R.layout.simple_list_item_1, tasks);


        holder.AddTaskTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());

                // Inflate the custom dialog layout
                View dialogView = ((LayoutInflater) holder.itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_add_task, null);
                builder.setView(dialogView)
                        .setTitle("Assign Task to-"+getEmail)
                        .setCancelable(false);

                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button addTask = dialogView.findViewById(R.id.addTask);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText Task = dialogView.findViewById(R.id.task);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ListView listView = dialogView.findViewById(R.id.list);
                listView.setAdapter(adapter);

                addTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getTask = Task.getText().toString();
                        if (!getTask.isEmpty())
                        {
                            tasks.add(getTask);
                            adapter.notifyDataSetChanged();
                            FirebaseDatabase.getInstance().getReference().child("Team")
                                    .child(TeamName).child(pos).child(volunteerEmail).child("Assigned Task")
                                    .child(Integer.toString(adapter.getCount())).setValue(getTask);

                            Task.setText("");
                            Task.requestFocus();

                         }
                         else {
                            Toast.makeText(holder.itemView.getContext(), "Please enter a task, then add", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
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
