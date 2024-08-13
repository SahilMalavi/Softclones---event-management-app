package com.example.navbotdialog;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class team extends Fragment {

    private RecyclerView recyclerView;
    private List<DataModel> mList;
    List<String> teamNames, list;
    List<List<String>> nestedList1;
    ArrayList<String> TaskList;

    private ItemAdapter_Team adapter;
    String TeamName, NameEmail, position,task;
    DatabaseReference TeamRef, AssignTaskRef;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        recyclerView = view.findViewById(R.id.team);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mList = new ArrayList<>();
        nestedList1 = new ArrayList<>();
        list = new ArrayList<>();
        teamNames = new ArrayList<>();
        TaskList=new ArrayList<>();
        fetchCurrentData();

        return view;
    }

    public void fetchCurrentData() {
        try {
            TeamRef = FirebaseDatabase.getInstance().getReference("Team");
            TeamRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    TaskList.clear();
                    mList.clear();
                    teamNames.clear();
                    for (DataSnapshot TEAMNAME : snapshot.getChildren()) {
                        List<String> list = new ArrayList<>(); // create new list object for each TeamName
                        TeamName = TEAMNAME.getKey();
                        if (!TeamName.isEmpty()) {

                            teamNames.add(TeamName);

                            for (DataSnapshot POSITION : TEAMNAME.getChildren()) {
                                position = POSITION.getKey();
                                for (DataSnapshot VDATA : POSITION.getChildren()) {
                                    String name = VDATA.child("name").getValue(String.class);
                                    String email = VDATA.child("email").getValue(String.class);
                                    String password = VDATA.child("password").getValue(String.class);
                                    NameEmail = position + " : Name- " + name + "\t |  " + email+ "\t |  " + password;
                                    list.add(NameEmail);
                                    //another for loops to fetch Assigned task of perticular volunteer
                                    for (DataSnapshot ASSIGNED_TASK : VDATA.child("Assigned Task").getChildren()) {
                                       String task = ASSIGNED_TASK.getValue(String.class);
                                        TaskList.add(task);
//                                        Toast.makeText(getContext(), ""+task, Toast.LENGTH_SHORT).show();                                        TaskList.add(task);
                                    }

                                }
                            }
                            nestedList1.add(list); // add the new list object to nestedList1
                            mList.add(new DataModel(nestedList1.get(nestedList1.size() - 1), TeamName));
                            adapter = new ItemAdapter_Team(mList, TaskList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

//                adp.notifyDataSetChanged();
//                BaseAdapter baseAdapter = (BaseAdapter) parent.getAdapter();
//                baseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }

//        for (DataSnapshot VolunteerTask : VDATA.getChildren()) {
//
//            String fetchedTask = VolunteerTask.child("1").getValue(String.class);
//            Toast.makeText(getContext(), "" +fetchedTask, Toast.LENGTH_SHORT).show();
//            for (DataSnapshot EachTask : VolunteerTask.getChildren()) {
//
////                                            TaskList.add(fetchedTask);
//            }
//        }

    }

}