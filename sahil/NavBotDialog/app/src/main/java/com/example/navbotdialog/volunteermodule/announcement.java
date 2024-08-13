package com.example.navbotdialog.volunteermodule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navbotdialog.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class announcement extends Fragment {


    private RecyclerView announcement1;
    announceAdapter announceAdapter;
    DatabaseReference reference;
    // annouceModelClass annouceModelClass ;
    ArrayList<annouceModelClass> list;

    String currentTime ;
    String currentDate ;

    private ArrayList<annouceModelClass> announceModelArrayList;
    private EditText userMsg;
    private TextView dateTime;
    private Button btnAdd;

    private AlertDialog alertDialog;

    public announcement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_announcement, container, false);


//        announceModelArrayList = new ArrayList<>();
//        userContact = view.findViewById(R.id.userMobile);
//        userName = view.findViewById(R.id.userName);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseApp.initializeApp(requireContext());

        list = new ArrayList<>();

        announceModelArrayList = new ArrayList<>();

        // time = view.findViewById(R.id.timeID);
        userMsg = view.findViewById(R.id.userMsg);
//        dateTime = view.findViewById(R.id.timeTxtId);


        btnAdd = view.findViewById(R.id.announce);

        announcement1 = view.findViewById(R.id.announce_list);

        announcement1.setHasFixedSize(true);

        announcement1.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        fetchCurrentData();
        btnAdd.setOnClickListener(v -> {
            btn_showDialog(view);
        });
    }
    public void fetchCurrentData() {
        try {
            reference = FirebaseDatabase.getInstance().getReference("Announcement").child("Team");
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for(DataSnapshot VEmail: snapshot.getChildren()){
                            for (DataSnapshot NewMsg : VEmail.getChildren()) {

                                    String msg = NewMsg.child("msg").getValue(String.class);
                                    String date = NewMsg.child("date").getValue(String.class);
                                    String time = NewMsg.child("time").getValue(String.class);
//                                Toast.makeText(getContext(), ""+msg+date, Toast.LENGTH_SHORT).show();
                                list.add(new annouceModelClass(msg,date,time));
                            }
                        announceAdapter = new announceAdapter(getContext(), list);
                            announceAdapter.notifyDataSetChanged();
                        }}
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(getContext(), "Please, Do previous process again!", Toast.LENGTH_SHORT).show();
        }
    }
    private void addContact(String msg) {

        annouceModelClass obj = new annouceModelClass();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm ");
        currentTime = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy ");
        currentDate = sdf1.format(new Date());

        obj.setUserMsg(msg);
        obj.setDate(currentDate);
        obj.setTime(currentTime);
        announceModelArrayList.add(obj);

        announceAdapter = new announceAdapter(getContext(), announceModelArrayList);
        announcement1.setAdapter(announceAdapter);

    }


    public void btn_showDialog(View view) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        // Inflate the custom dialog layout
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_volunteer, null);
        builder.setView(mView)
                .setTitle("Announcement")
                .setCancelable(false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final TextView time = mView.findViewById(R.id.timeTxtId);
        final EditText msg = (EditText) mView.findViewById(R.id.userMsg);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Spinner spinner = mView.findViewById(R.id.spinner);

        Button btn_cancle = (Button) mView.findViewById(R.id.btn_cancle);
        Button btn_ok = (Button) mView.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedValue = spinner.getSelectedItem().toString();
                String strUserMsg = String.valueOf(msg.getText());

                reference = FirebaseDatabase.getInstance().getReference("Announcement").child(selectedValue).child("sahil@gmail_com").push();
                reference.child("name").setValue("Sanika");
                reference.child("msg").setValue(strUserMsg);
                reference.child("time").setValue(currentTime);
                reference.child("date").setValue(currentDate);

                addContact(strUserMsg);
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", null);

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }
}