package com.example.navbotdialog.volunteermodule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navbotdialog.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class myTeam extends Fragment {
    private RecyclerView recyclerView;
    ItemAdapter_Team itemAdapter;
    private List<DataModel> mList;
    List<String> teamNames, list;
    List<List<String>> nestedList1;DatabaseReference TeamRef;
    String TeamName, position, NameEmail;

    public myTeam() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_team2, container, false);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

// Events

        recyclerView = view.findViewById(R.id.recycleview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mList = new ArrayList<>();
        nestedList1 = new ArrayList<>();
        list = new ArrayList<>();
        teamNames = new ArrayList<>();

        fetchCurrentData();

    }
    public void fetchCurrentData() {
        try {
            TeamRef = FirebaseDatabase.getInstance().getReference("Team");
            TeamRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    teamNames.clear();
                    for (DataSnapshot TEAMNAME : snapshot.getChildren()) {
                        List<String> list = new ArrayList<>(); // create new list object for each TeamName
                        TeamName = TEAMNAME.getKey();
                        if (!TeamName.equals("")) {

                            teamNames.add(TeamName);

                            for (DataSnapshot POSITION : TEAMNAME.getChildren()) {
                                position = POSITION.getKey();
                                for (DataSnapshot VDATA : POSITION.getChildren()) {
                                    String name = VDATA.child("name").getValue(String.class);
                                    String email = VDATA.child("email").getValue(String.class);
                                    NameEmail = position + " : Name- " + name + "\t |  " + email;
                                    list.add(NameEmail);
                                }
                            }
                            nestedList1.add(list); // add the new list object to nestedList1
                            mList.add(new DataModel(nestedList1.get(nestedList1.size() - 1), TeamName));

                            itemAdapter = new ItemAdapter_Team(mList);
                            recyclerView.setAdapter(itemAdapter);
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
        }

    }


}