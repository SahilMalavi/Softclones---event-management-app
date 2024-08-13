package com.example.navbotdialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class usercommittee extends Fragment {
    private RecyclerView recyclerView;
    private TextView selectedItemtv;
    ItemAdapter itemAdapter;
    NestedAdapter nestedAdapter;
    private List<DataModel> mList;
    List<String> teamNames, PosNameList, list;
    List<List<String>> nestedList1;
    ArrayAdapter<String> adp;
    private ItemAdapter adapter;
    Spinner selTeam;
    DatabaseReference TeamRef;
    String TeamName, position, NameEmail, selectedOption, selectedPosition;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_committee, container, false);

        recyclerView = view.findViewById(R.id.team);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mList = new ArrayList<>();
        nestedList1 = new ArrayList<>();
        list = new ArrayList<>();
        teamNames = new ArrayList<>();
        PosNameList = new ArrayList<>();
        PosNameList.add("Member");
        PosNameList.add("Coordinator");
        adp = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, teamNames);

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

                            adapter = new ItemAdapter(mList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Please, Do previous process again!", Toast.LENGTH_SHORT).show();
        }

    }

}