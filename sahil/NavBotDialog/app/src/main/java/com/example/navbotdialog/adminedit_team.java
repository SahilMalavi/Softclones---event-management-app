package com.example.navbotdialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class adminedit_team extends Fragment {
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
    String TeamName, position, NameEmail, selectedOption, selectedPosition,mailErr;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.adminedit_team, container, false);
        selTeam = view.findViewById(R.id.selTeam);
        selectedItemtv = view.findViewById(R.id.choose);
        FloatingActionButton addTeam = view.findViewById(R.id.fab);
        Button addVolunteer = view.findViewById(R.id.addVolunteer);
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
//Add team
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeam();
            }
        });
//Add volunteer
        addVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVolunteer();
            }
        });
        selTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedOption = teamNames.get(pos);
                selectedItemtv.setText(selectedOption);
//                BaseAdapter baseAdapter = (BaseAdapter) parent.getAdapter();
//                baseAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                    //set Adapter to spinner
                    selTeam.setAdapter(adp);


//                adp.notifyDataSetChanged();
//                BaseAdapter baseAdapter = (BaseAdapter) parent.getAdapter();
//                baseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Please, Do previous process again!", Toast.LENGTH_SHORT).show();
        }

    }

    void addTeam()  {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //uploading progressbar
        AlertDialog.Builder progress = new AlertDialog.Builder(getContext());
        progress.setCancelable(false);
        progress.setView(R.layout.progress_dialog);
        AlertDialog progressbar = progress.create();

        // Inflate the custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_team, null);
        builder.setView(dialogView)
                .setTitle("Create New Team")
                .setCancelable(false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText TeamName = dialogView.findViewById(R.id.TeamName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button create = dialogView.findViewById(R.id.createTeam);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Teamtitle = TeamName.getText().toString();
                if (!Teamtitle.contains(".") && !Teamtitle.contains("$") && !Teamtitle.contains("#") && !Teamtitle.contains("[") && !Teamtitle.contains("]")) {
                    if (!Teamtitle.isEmpty()) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Team");
                        reference.child(Teamtitle).setValue("");

                        TeamName.setText("");
                        TeamName.requestFocus();
                        Toast.makeText(getActivity().getApplicationContext(), "Team created successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Please fill Team name first", Toast.LENGTH_SHORT).show();
                        progressbar.dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Error! Team name must not contain '.', '#', '$', '[', or ']' ", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);


        AlertDialog dialog = builder.create();
        dialog.show();

    }

    void addVolunteer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //uploading progressbar
        AlertDialog.Builder progress = new AlertDialog.Builder(getContext());
        progress.setCancelable(false);
        progress.setView(R.layout.progress_dialog);
        AlertDialog progressbar = progress.create();

        // Inflate the custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_volunteer, null);
        builder.setView(dialogView)
                .setTitle("Add volunteer in " + selectedOption)
                .setCancelable(false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText Vname = dialogView.findViewById(R.id.Vname);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText Vemail = dialogView.findViewById(R.id.Vemail);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText Vpass = dialogView.findViewById(R.id.password);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button add = dialogView.findViewById(R.id.addV);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Spinner selPos = dialogView.findViewById(R.id.selPos);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView dataTv = dialogView.findViewById(R.id.data);

//        Add spinner list

        ArrayAdapter<String> adp = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, PosNameList);
        selPos.setAdapter(adp);
        //fetch spinner selected item
        selPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedPosition = PosNameList.get(pos);
                dataTv.setText(selectedPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Vname.getText().toString();
                String email = Vemail.getText().toString();
                String pass = Vpass.getText().toString();

                String regex = "^[\\w!#$%&'+/=?`{|}~^-]+(?:\\.[\\w!#$%&'+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

                //Compile regular expression to get the pattern
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);
                if(matcher.matches()){

                    mailErr = "";
                    Vemail.setError(null);

                }else{
                    mailErr="Enter correct email id";
                    Vemail.setError(mailErr);
                }
                if (!name.isEmpty() && !email.isEmpty() &&!pass.isEmpty()) {
                    String dupEmail = email.replace(".", "_");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Team").child(selectedOption).child(selectedPosition).child(dupEmail);
                    reference.child("name").setValue(name);
                    reference.child("email").setValue(email);
                    reference.child("password").setValue(pass);
                    Vname.setText("");
                    Vemail.setText("");
                    Vpass.setText("");
                    Vname.requestFocus();
                    Toast.makeText(getActivity().getApplicationContext(), "Volunteer added successfully", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getContext(), "Please fill all the above fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void refreshFragment() {

//        getFragmentManager().beginTransaction().detach(adminedit_team.this).attach(adminedit_team.this).commit();
//        Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
    }

}