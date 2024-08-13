package com.example.navbotdialog;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Registration extends AppCompatActivity {
    public static final int PICK_IMAGES_REQUEST=1;
    boolean paymentImageFlag=false;
    DatabaseReference userRegistration;
    Bundle bundle;
//    ArrayList<String> events = new ArrayList<>();
String[] events={"C Coder","Paper Presenation","Poster Presenataion","Chess Titan","Quiz Master"};
List<String> eventNames=new ArrayList<>();
    String eventImg;
    String eventName;
    String paymentMode="";
    String eventDesc;
    FirebaseDatabase getEvents;


    //creating references to set information about events which can be get from home page.
    ImageView thisEventImage;
    TextView thisEventName;

    //creating refercens of registration form fields.
    FloatingActionButton floatingActionButton;
    TextInputEditText firstName,middleName,lastName,emailId,branch, learningYear, collegeName, whatsappNumber,transactionId,paymentRecieverNameRF;
    AutoCompleteTextView eventNameRF, noOfParticipants;
    RadioButton onlineMode, offlineMode;
    RadioGroup radioButtons;
    Button register, upiPayment;
    RelativeLayout addScreenshotOptions;
    TextInputLayout transactionIdOption, paymentRecieverNameOption;
    ImageView paymentScreenshot;
    String getPaymentReciever;

    //datatypes for validation
    String getFName,getMName,getLName,getEmail,getBranch,getLearningYear,getCollegeName,
            getNumber,getEventName,getParticipantsCount,getTransactionId="";

    String fNameErr,mNameErr,lNameErr,mailErr,branchErr,learningYearErr,collegeNameErr,numberErr, eventErr,countErr,transactionIdErr="";

    final int UPI_PAYMENT=0;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //bundle object created to get content passed from past activity.
        bundle = getIntent().getExtras();

        //.this function sets all essential data on this activity such as image, name and rules of event
        setAllData();
        registrationForm();

    }
    private void registrationForm() {

        register = findViewById(R.id.submit);
        onlineMode = findViewById(R.id.onlineMode);
        offlineMode = findViewById(R.id.offlineMode);
        upiPayment = findViewById(R.id.upiPay);
        addScreenshotOptions = findViewById(R.id.addScrenshotOptions);
        transactionIdOption = findViewById(R.id.transactionIdOption);
        paymentRecieverNameOption = findViewById(R.id.paymentRecieverName);
        floatingActionButton = findViewById(R.id.floatingActionButton);


        firstName = findViewById(R.id.firstNameField);
        middleName = findViewById(R.id.middleNameField);
        lastName = findViewById(R.id.lastNameField);
        emailId = findViewById(R.id.emailField);
        branch = findViewById(R.id.tradeField);
        learningYear = findViewById(R.id.yearField);
        collegeName = findViewById(R.id.collegeField);
        whatsappNumber = findViewById(R.id.whatsappNumberField);
        eventNameRF = findViewById(R.id.eventField);
        noOfParticipants = findViewById(R.id.participantsField);
        paymentScreenshot = findViewById(R.id.imageView);
        transactionId = findViewById(R.id.transactionIdField);
        paymentRecieverNameRF = findViewById(R.id.paymentRecieverNameField);


        formValidationCode();

        //this code helps to get screenshot of payment from user.
        floatingActionButton.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGES_REQUEST);
            }
        });
        onlineMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upiPayment.setVisibility(View.VISIBLE);
                addScreenshotOptions.setVisibility(View.VISIBLE);
                transactionIdOption.setVisibility(View.VISIBLE);
                paymentRecieverNameOption.setVisibility(View.GONE);
            }
        });
        offlineMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upiPayment.setVisibility(View.GONE);
                addScreenshotOptions.setVisibility(View.GONE);
                transactionIdOption.setVisibility(View.GONE);

                paymentRecieverNameOption.setVisibility(View.VISIBLE);
            }
        });

        //listerner registered for upi payment option
        upiPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUsingUpi();

            }
        });


        register.setOnClickListener(view -> {

            if(getFName==null){
                firstName.setError("Name must not be empty");
                firstName.requestFocus();
                return;
            }
            if(getMName==null){
                middleName.setError("Name must not be empty");
                middleName.requestFocus();
                return;
            }
            if(getLName==null){
                lastName.setError("Name must not be empty");
                lastName.requestFocus();
                return;
            }
            if(getEmail==null){
                emailId.setError("Email must not be empty");
                emailId.requestFocus();
                return;
            }
            if(getBranch==null){
                branch.setError("Email must not be empty");
                branch.requestFocus();
                return;
            }
            getLearningYear = learningYear.getText().toString();
            if(getLearningYear==null){
                learningYear.setError("Learning Year must not be empty");
                learningYear.requestFocus();
                return;
            }
            if(getCollegeName==null){
                collegeName.setError("Collage name must not be empty");
                collegeName.requestFocus();
                return;
            }
            if(getNumber==null){
                whatsappNumber.setError("Mobile number must not be empty");
                whatsappNumber.requestFocus();
                return;
            }
            getParticipantsCount = noOfParticipants.getText().toString();
            if(getParticipantsCount==null){
                noOfParticipants.setError("Kindly enter no. of participants");
                noOfParticipants.requestFocus();
                return;
            }

            getTransactionId=transactionId.getText().toString();

            if(onlineMode.isSelected()){
                if(paymentImageFlag==false){
                    Toast.makeText(Registration.this, "Please upload payment screenshot.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(getTransactionId==null){
                    transactionId.setError("Enter transaction id");
                    transactionId.requestFocus();
                    return;
                }
            }

            if(offlineMode.isSelected()){
                if(paymentRecieverNameRF.getText().toString()==null){
                    paymentRecieverNameRF.setError("Please enter payment recievers name");
                    paymentRecieverNameRF.requestFocus();
                    return;
                }
            }

            insertToDatabase();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();




        });





    }

    private void formValidationCode() {

        firstName.setOnFocusChangeListener((view, b) -> {
            getFName = firstName.getText().toString();

            if(getFName.matches( "[A-Z][a-z]*" )){
                getFName = firstName.getText().toString();
                fNameErr="";
                firstName.setError(null);
            }
            else{
                fNameErr= "Enter correct name";
                firstName.setError(fNameErr);
            }
        });

        middleName.setOnFocusChangeListener((view, b) -> {
                    getMName = middleName.getText().toString();

                    if (getMName.matches("[A-Z][a-z]*")) {
                        getMName = middleName.getText().toString();
                        mNameErr = "";
                        middleName.setError(null);
                    } else {
                        mNameErr = "Enter correct name";
                        middleName.setError(mNameErr);
                    }
                });

        lastName.setOnFocusChangeListener((view, b) -> {
            getLName = lastName.getText().toString();

            if (getLName.matches("[A-Z][a-z]*")) {
                getLName =lastName.getText().toString();
                lNameErr = "";
                lastName.setError(null);
            } else {
                lNameErr = "Enter correct name.";
                lastName.setError(lNameErr);
            }
        });

        emailId.setOnFocusChangeListener((view, b) -> {

            getEmail = emailId.getText().toString();

            String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

            //Compile regular expression to get the pattern
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(getEmail);
            if(matcher.matches()){

                getEmail = emailId.getText().toString();
                mailErr = "";
                emailId.setError(null);

            }else{
                mailErr="Enter correct email id";
                emailId.setError(mailErr);
            }
        });

        branch.setOnFocusChangeListener((view, b) -> {
            getBranch = branch.getText().toString();

            if (getBranch.matches("[A-Z][a-z]* [A-Z][a-z ]*")) {
                getBranch =branch.getText().toString();
                branchErr = "";
                branch.setError(null);
            } else {
                branchErr = "Branch Name must be alphabetic only.";
                branch.setError(branchErr);
            }
        });

        collegeName.setOnFocusChangeListener((view, b) -> {
            getCollegeName = collegeName.getText().toString();

            if (getCollegeName.matches("[[A-Z][a-z]* [A-Z][a-z]*]*")) {
                getCollegeName =collegeName.getText().toString();
                collegeNameErr = "";
                collegeName.setError(null);
            } else {
                collegeNameErr = "College Name must be alphabetic only.";
                collegeName.setError(collegeNameErr);
            }
        });

        whatsappNumber.setOnFocusChangeListener((view, b) -> {
            getNumber = whatsappNumber.getText().toString();
            getNumber.trim();

            Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");

            //the matcher() method creates a matcher that will match the given input against this pattern
            Matcher match = ptrn.matcher(getNumber);

            if(match.matches()){
                getNumber = whatsappNumber.getText().toString();
                getNumber.trim();
                numberErr="";
                whatsappNumber.setError(null);
            }
            else{
                numberErr="Enter correct mobile number";
                whatsappNumber.setError(numberErr);
            }
        });
    }
    private void insertToDatabase() {

        getFName = firstName.getText().toString().trim();
        getMName = middleName.getText().toString().trim();
        getLName = lastName.getText().toString().trim();
        getEmail = emailId.getText().toString().trim();
        getBranch = branch.getText().toString().trim();
        getLearningYear = learningYear.getText().toString().trim();
        getCollegeName = collegeName.getText().toString().trim();
        getNumber = whatsappNumber.getText().toString().trim();
        getEventName = eventNameRF.getText().toString();
        getParticipantsCount = noOfParticipants.getText().toString();

        getTransactionId = transactionId.getText().toString();
        getPaymentReciever = paymentRecieverNameRF.getText().toString().trim();

        key = getNumber;

        userRegistration = FirebaseDatabase.getInstance().getReference().child("UserRegistration").child(getEventName);

        GetRegisterationData getRegisterationData = new GetRegisterationData(getFName,getMName,getLName,getEmail,
                getBranch,getLearningYear, getCollegeName,
                getNumber,getEventName,getParticipantsCount,
                paymentMode,getTransactionId,getPaymentReciever);
        assert key !=null;
        userRegistration.child(key).setValue(getRegisterationData);

    }



    /*--------------------------program to do upi payment---------------------------*/
    private void payUsingUpi() {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa","9850528634@axl")
                .appendQueryParameter("am","1")
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay With");

        if (null!=chooser.resolveActivity(getPackageManager()))
        {

            startActivityForResult(chooser,UPI_PAYMENT);
        }
        else
        {
            Toast.makeText(Registration.this, "No UPI app found, please install to continue", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case UPI_PAYMENT:
                if (RESULT_OK==resultCode || (resultCode == 11))
                {
                    if (data!=null)
                    {

                        String trtx = data.getStringExtra("response");
                        Log.d("UPI","onActivityResult"+trtx);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trtx);
                        upiPaymentDataOperation(dataList);
                    }
                    else
                    {
                        Log.d("UPI","onActivityResult"+"Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }
                else
                {
                    Log.d("UPI","onActivityResult"+"Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);

                }
                break;

            case PICK_IMAGES_REQUEST:
                if(resultCode==RESULT_OK){
                    Uri selectedImageUri =  data.getData();
                    if(selectedImageUri!=null){
                        paymentScreenshot.setImageURI(selectedImageUri);
                        paymentImageFlag = true;
                    }
                    else{
                        paymentImageFlag=false;
                    }
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data){
        if (isConnectionAvailable(Registration.this))
        {
            String str = data.get(0);
            Log.d("UPIPAY","upiPaymentOperation:"+str);
            String paymentCancel = "";

            if (str == null) str="discard";
            String status="";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(Registration.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Registration.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Registration.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Registration.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    /*--------------------------------------------------------------------------*/

    private void setAllData() {

         eventImg = bundle.getString("eventImg");
         eventName = bundle.getString("eventName");
         eventDesc = bundle.getString("eventDesc");

         thisEventImage = findViewById(R.id.eventImageR);
         thisEventName = findViewById(R.id.eventNameR);

        Glide.with(getApplicationContext())
                .load(eventImg)
                .into(thisEventImage);

        thisEventName.setText(eventName);

        eventNameRF = findViewById(R.id.eventField);

        arrayAdapter= new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,events);
        eventNameRF.setAdapter(arrayAdapter);

        eventNameRF.setText(eventName);


//        eventNameRF = findViewById(R.id.eventField);


//        getEvents.getInstance().getReference("events")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot eventName:snapshot.getChildren()){
//                           events[events.length]=eventName.getValue(String.class);
//                        }
//
//                        arrayAdapter= new ArrayAdapter<>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,events);
//                        eventNameRF.setAdapter(arrayAdapter);
//
//                        eventNameRF.setText(eventName);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

//        eventNameRF.setText(eventName);
    }
    String key;
}