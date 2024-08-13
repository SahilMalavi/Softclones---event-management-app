package com.example.navbotdialog;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adminedit_ui extends Fragment {
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    Uri uri, filePath;
    ImageView cover, insertedImage;
    public int i = 0, j = 0;
    public static String key;
    public static int PICK_IMAGES_REQUEST = 1;

    Button insert;
    TextView ename, editEname;
    RecyclerView rclv;
    EventAdapter eventAdapter;
    FloatingActionButton fab;
    FirebaseDatabase db;
    FirebaseStorage storage;
    private boolean isImageSelected = false;
    private boolean ImageSelected = false;
    Intent intent;
    DatabaseReference eventsRef, imagesRef;
    ArrayList<EventModel> list;

    String Etitle, Edescription,Ename;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.adminedit_ui, container, false);

        rclv = (RecyclerView) view.findViewById(R.id.eventList);
        rclv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity().getApplicationContext(), list);
        rclv.setAdapter(eventAdapter);


        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        cover = view.findViewById(R.id.cover);
        ename = view.findViewById(R.id.ename);
        editEname = view.findViewById(R.id.edit_Ename);
        fab = view.findViewById(R.id.fab);
        insert = view.findViewById(R.id.insert);

        editEname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEventName();
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showInsertDialog();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGES_REQUEST);

                //                ImagePicker.with(adminedit_ui.this)
//                        .crop()                    //Crop image(Optional), Check Customization for more option
//                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
//            }
//        });
            }
        });

        imagesRef = FirebaseDatabase.getInstance().getReference().child("image");
        Query latestImageQuery = imagesRef.orderByKey().limitToLast(1);
        latestImageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the download URL of the image stored under the latest key.
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String downloadUrl = childSnapshot.getValue(String.class);
                    // Toast.makeText(getActivity().getApplicationContext(), "" + downloadUrl, Toast.LENGTH_SHORT).show();
                    Picasso.get().load(downloadUrl).into(cover);
                }
                viewUpdatedEname();
            }

            //
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here.
            }
        });


//        db.getReference().child("image").child("image0")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                     String imgUrl=snapshot.getValue(String.class);
//                        Picasso.get()
//                                .load(snapshot.getValue(String.class))
//                                .into(cover);
//                    Toast.makeText(getActivity().getApplicationContext(), ""+imgUrl, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (isImageSelected) {
                        getActivity().finish();
                        return true;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setMessage("Do you want to exit ?");
                        builder.setTitle("Alert !");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                            getActivity().finish();
                        });

                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        return true;
                    }
                }
                return false;
            }
        });

//fetch events database

        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ename="";
                Ename = snapshot.child("Event Name").getValue(String.class);
                ename.setText(Ename);
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //  EventModel model1=dataSnapshot.getValue(EventModel.class);
                    String title = dataSnapshot.child("e_title").getValue(String.class);
                    String des = dataSnapshot.child("e_description").getValue(String.class);
                    String img = dataSnapshot.child("e_img").getValue(String.class);

                    list.add(new EventModel(title, des, img));
                }
                eventAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

    //update event name
    private void editEventName() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //uploading progressbar
        AlertDialog.Builder progress = new AlertDialog.Builder(getContext());
        progress.setCancelable(false);
        progress.setView(R.layout.progress_dialog);
        AlertDialog progressbar = progress.create();

        // Inflate the custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.insert_event_name, null);
        builder.setView(dialogView)
                .setTitle("Update Event Name")
                .setCancelable(false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText title = dialogView.findViewById(R.id.Ename);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button insert = dialogView.findViewById(R.id.insertName);


        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etitle = title.getText().toString();
                progressbar.show();

                if (!Etitle.equals("")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Event Name");
                    reference.setValue(Etitle);
                    title.setText("");
                    title.requestFocus();
                    Toast.makeText(getActivity().getApplicationContext(), "Event name changed successfully", Toast.LENGTH_SHORT).show();
                    progressbar.dismiss();
                } else {
                    Toast.makeText(getContext(), "Please fill event name first", Toast.LENGTH_SHORT).show();
                    progressbar.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //create,insert new event in alert dialog
    private void showInsertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //uploading progressbar
        AlertDialog.Builder progress = new AlertDialog.Builder(getContext());
        progress.setCancelable(false);
        progress.setView(R.layout.progress_dialog);

        AlertDialog progressbar = progress.create();

        // Inflate the custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_insert_custom, null);
        builder.setView(dialogView)
                .setTitle("Create New Event")
                .setCancelable(false);

        EditText title = dialogView.findViewById(R.id.edit1);
        EditText description = dialogView.findViewById(R.id.edit2);
        FloatingActionButton uploadImg = dialogView.findViewById(R.id.uploadImg);
        Button insert = dialogView.findViewById(R.id.insert);


        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, 2);

            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etitle = title.getText().toString();
                Edescription = description.getText().toString();
                progressbar.show();
                if (!Etitle.equals("") && !Edescription.equals("") && ImageSelected == true) {


                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("EventImageFolder");

                    StorageReference imageRef = storageRef.child(filePath.getLastPathSegment());
                    imageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded image and store it under a key in the Realtime Database.
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("events").child(Etitle);
                                    reference.child("e_title").setValue(Etitle);
                                    reference.child("e_description").setValue(Edescription);
                                    reference.child("e_img").setValue(downloadUrl);
                                    title.setText("");
                                    description.setText("");
                                    title.requestFocus();
                                    Toast.makeText(getActivity().getApplicationContext(), "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                                    progressbar.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Please fill all the above fields", Toast.LENGTH_SHORT).show();
                progressbar.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //show image of event in imageview
        if (requestCode == 2 && resultCode == RESULT_OK) {

            filePath = data.getData();
            Toast.makeText(getContext(), "Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            isImageSelected = true;
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            ImageSelected = true;
        }
//upload slider images multiple and single
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("imageFolder");
            //uploading progressbar
            AlertDialog.Builder progress = new AlertDialog.Builder(getContext());
            progress.setCancelable(false);
            progress.setView(R.layout.progress_dialog);
            AlertDialog progressbar = progress.create();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to upload ?");
            builder.setTitle("Upload Images");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            progressbar.show();
                if (isImageSelected) {
                    if (data.getClipData() != null) {
                        // Multiple images selected.
                        int count = data.getClipData().getItemCount();
                        for (i = 0; i < count; i++) {
                            Uri filePath = data.getClipData().getItemAt(i).getUri();
                            StorageReference imageRef = storageRef.child(filePath.getLastPathSegment());
                            String downloadUrl = filePath.toString();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("image");

                            imageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            key = "image" + j;
                                            reference.child(key).setValue(uri.toString());

                                            Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            cover.setImageURI(filePath);
                                            j += 1;
                                            progressbar.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        // Single image selected.
                        Uri filePath = data.getData();
                        StorageReference imageRef = storageRef.child(filePath.getLastPathSegment());
                        imageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the download URL of the uploaded image and store it under a key in the Realtime Database.
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("image");
                                        key = "image";
                                        reference.child(key).setValue(downloadUrl);
                                        Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        cover.setImageURI(filePath);
                                        progressbar.dismiss();
                                    }
                                });
                            }
                        });
                    }
                } else {

                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setTitle("Upload Image");
                    b.setMessage("Please select at least one Image");
                    b.setCancelable(true);
                    AlertDialog al = b.create();
                    al.show();
                }
            });

            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }

    public void viewUpdatedEname() {

//fetch changed event name
        eventsRef = FirebaseDatabase.getInstance().getReference("Event Name");
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                ename.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}