package com.example.navbotdialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link userHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public userHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static userHome newInstance(String param1, String param2) {
        userHome fragment = new userHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View view;
    RecyclerView rclv;
    UserEventAdapter userEventAdapter;
    ImageSlider imageSlider;
    Button youtube;
    TextView eventName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_home, container, false);

        getSliderImages();
        getEventName();
        getEventsInfo();

        //to open youtube channel of softclones
        youtube=view.findViewById(R.id.visitYoutube);
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/@softclones/featured"));
                Intent chooser = Intent.createChooser(intent,"Launch Youtube");
                startActivity(chooser);
            }
        });

        return view;
    }

    //this function is defined to add infromation of events from firebase database to our application at user side.
    //user can see real time data updated by admin.
    private void getEventsInfo() {

        ArrayList<UserEventModel> eventList = new ArrayList<>();
        rclv = (RecyclerView) view.findViewById(R.id.eventListuser);
        rclv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
//        rclv.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FirebaseDatabase.getInstance().getReference().child("events")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String title = dataSnapshot.child("e_title").getValue(String.class);
                            String description = dataSnapshot.child("e_description").getValue(String.class);
                            String image = dataSnapshot.child("e_img").getValue(String.class);

                            // Check if any of the values is null before adding to the list
                            if (title != null && description != null && image != null) {
                                eventList.add(new UserEventModel(title, description, image));
                            }
                        }

                        userEventAdapter = new UserEventAdapter(getContext(), eventList);
                        rclv.setAdapter(userEventAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getEventName() {

        eventName = view.findViewById(R.id.eventName);
        FirebaseDatabase.getInstance().getReference("Event Name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object value = snapshot.getValue();
                        if (value != null) {
                            String name = value.toString();
                            eventName.setText(name);
                        } else {
                            // Handle the case where the value is null, e.g., set a default text
                            eventName.setText("Default Event Name");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    //slider images are get in runtime using this function
    private void getSliderImages() {
        imageSlider = view.findViewById(R.id.image_slider);
        final List<SlideModel> slideModels = new ArrayList<SlideModel>();

        FirebaseDatabase.getInstance().getReference("image")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            slideModels.add(new SlideModel(dataSnapshot.getValue().toString(), ScaleTypes.CENTER_CROP));
                        }
                        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}