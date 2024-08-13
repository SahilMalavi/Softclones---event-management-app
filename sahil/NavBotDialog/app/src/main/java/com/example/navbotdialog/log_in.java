package com.example.navbotdialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class log_in extends AppCompatActivity {
    Spinner loginOptions;
    ArrayAdapter<String> arrayAdapter;
    String options[]={"User","Volunteer"};
    TextInputEditText mobile,password;
    String selected;
    TextView signUpText;
    Button logIn;
    String mobileString,passString;

    DatabaseReference userInfoReference;
    String getMobFromDatabase;
    String getNameFromDatabase;
    String getPassFromDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        final String credentials = sharedPreferences.getString("name","");
        if(credentials.isEmpty()){

        }
        else{
            finish();
        }

        addOptionsToSpinner();

        signUpText = findViewById(R.id.textView5);
        mobile = findViewById(R.id.mobile);
        password=findViewById(R.id.passwordlogin);
        logIn = findViewById(R.id.login);

        loginOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(log_in.this, "Selected "+selected, Toast.LENGTH_SHORT).show();
                if(selected.equalsIgnoreCase("Volunteer")){
                    signUpText.setVisibility(View.GONE);
                    mobile.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                if(selected.equalsIgnoreCase("User")){
                    signUpText.setVisibility(View.VISIBLE);
                    mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selected.equalsIgnoreCase("volunteer")){

                    mobileString=mobile.getText().toString();
                    passString = password.getText().toString();

                    if(mobileString.equals("volunteer")){
                        if(passString.equals("1234")){
                            Toast.makeText(log_in.this, "Volunteer Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),com.example.navbotdialog.volunteermodule.MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        else{
                            Toast.makeText(log_in.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        Toast.makeText(log_in.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
                    }


                    if(mobileString.equals("admin")){
                        if(passString.equals("1234")){

                            Toast.makeText(log_in.this, "Admin Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),AdminMainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }else{
                            Toast.makeText(log_in.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(log_in.this, "Incorrect username", Toast.LENGTH_SHORT).show();
                    }
                }

                if(selected.equalsIgnoreCase("user")){

                mobileString = mobile.getText().toString().trim();
                passString = password.getText().toString().trim();

                if(mobileString.isEmpty()){
                    Toast.makeText(log_in.this, "Please enter mobile no.", Toast.LENGTH_SHORT).show();
                    mobile.requestFocus();
                    return;
                }
                if(passString.isEmpty()){
                    Toast.makeText(log_in.this, "Please enter password ", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }
                int flag=0;


                    FirebaseDatabase.getInstance().getReference().child("LoginInformation").child("User")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for(DataSnapshot data: snapshot.getChildren()){

                                            if(mobileString.equals(data.child("mobile").getValue().toString())){

                                                if(passString.equals(data.child("password").getValue().toString())){

                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("mobile",mobileString);
                                                    editor.putString("password",passString);
                                                    editor.putString("name",data.child("name").getValue().toString());
                                                    editor.commit();

                                                    Toast.makeText(log_in.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    return;
                                                }else{
                                                    Toast.makeText(log_in.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                                }

                                            }else{
                                                Toast.makeText(log_in.this, "User not found please register first.", Toast.LENGTH_SHORT).show();
                                            }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(log_in.this,signUp.class);
                startActivity(intent);
            }
        });


    }


    private void addOptionsToSpinner() {
        loginOptions=findViewById(R.id.loginOptions);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loginOptions.setAdapter(arrayAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        final String credentials = sharedPreferences.getString("mobile","");



        if(credentials.isEmpty()){
            Toast.makeText(this, "Please Login ", Toast.LENGTH_SHORT).show();
        }
        else{

            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        final String credentials = sharedPreferences.getString("mobile","");

        if(credentials.isEmpty()){
            Toast.makeText(this, "Please Login ", Toast.LENGTH_SHORT).show();
        }
        else{
            finish();
        }



    }
}