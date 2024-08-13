package com.example.navbotdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signUp extends AppCompatActivity {


    EditText nameEditText;
    EditText mobileNumberEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    TextView loginMove;
    Button signup;

    DatabaseReference userAuth;
    String name,mobile,createPass, confirmPass,key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginMove = findViewById(R.id.textView7);
        nameEditText = findViewById(R.id.name);
        mobileNumberEditText = findViewById(R.id.mobileNumber);
        passwordEditText = findViewById(R.id.createPass);
        confirmPasswordEditText = findViewById(R.id.confirmPass);

        signup= findViewById(R.id.signup);

        userAuth = FirebaseDatabase.getInstance().getReference().child("LoginInformation").child("User");
        final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

       signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean mobFlag = false;
                String str = "";

                name=nameEditText.getText().toString().trim();
                mobile = mobileNumberEditText.getText().toString();
                createPass = passwordEditText.getText().toString();
                confirmPass = confirmPasswordEditText.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(signUp.this, "Enter name", Toast.LENGTH_SHORT).show();
                    nameEditText.requestFocus();
                    return;
                }

                if (mobile.isEmpty()) {
                    Toast.makeText(signUp.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                    mobileNumberEditText.requestFocus();
                    return;
                } else {
                    if (isValidMobileNo(mobile))
                        mobFlag = true;
                    else
                        mobFlag = false;
                }

                if (mobFlag) {
                    //if mobile no. is valid then check both passwords. if passwords are equal then register the user and go to log in page.
                    //this code will be written over here. in if statement.
                    if (createPass.equals(confirmPass)) {
                        if (createPass.length() > 6 || confirmPass.length() > 6) {
                            Toast.makeText(signUp.this, "Pass length must be less than 6", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (createPass.length() < 3 || confirmPass.length() < 3) {
                            Toast.makeText(signUp.this, "Pass length must be greater than 3", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            //program to insert data in firebase
                            insertToDatabase();
                            
                        }
                    } else {
                        Toast.makeText(signUp.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                        confirmPasswordEditText.requestFocus();
                    }
                } else {
                    Toast.makeText(signUp.this, "Invalid Mobile", Toast.LENGTH_SHORT).show();
                    mobileNumberEditText.requestFocus();
                }
            }
        });
        loginMove.setOnClickListener(view -> {
            finish();
        });
        }

    private void insertToDatabase() {

        mobile = mobileNumberEditText.getText().toString().trim();
        name = nameEditText.getText().toString().trim();
        confirmPass = passwordEditText.getText().toString().trim();
        key=mobile;

        Users users = new Users(mobile,name,confirmPass);
        assert key != null;
        userAuth.child(key).setValue(users);
        Toast.makeText(signUp.this,"User Registered Successfully",Toast.LENGTH_LONG).show();

        final SharedPreferences sharedPreferences = getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",name);
        editor.putString("mobile",mobile);
        editor.putString("password",confirmPass);
        editor.commit();

        Toast.makeText(this, "Welcome "+name, Toast.LENGTH_SHORT).show();
        finish();
    }
    public static boolean isValidMobileNo(String str){
        Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher match = ptrn.matcher(str);
        return (match.find() && match.group().equals(str));
        }
}