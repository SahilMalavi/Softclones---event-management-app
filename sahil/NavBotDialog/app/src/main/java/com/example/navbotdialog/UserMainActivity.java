package com.example.navbotdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class UserMainActivity extends AppCompatActivity {

//    FloatingActionButton fab ;
    DrawerLayout drawerLayout ;
    BottomNavigationView bottomNavigationView ;
    NavigationView navigationView;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.profile, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
    public void getSharedPref(){

        Menu menu = navigationView.getMenu();
        MenuItem menuItem=menu.findItem(R.id.nav_login);
        MenuItem menuLogout=menu.findItem(R.id.nav_logout);

        final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        final String credentials = sharedPreferences.getString("mobile","");
        final String name = sharedPreferences.getString("name","");
//        Toast.makeText(this, "Welcome "+name, Toast.LENGTH_SHORT).show();

        if(name.isEmpty()){
            menuItem.setVisible(true);
//            menuLogout.setVisible(false);

        }else{
            menuItem.setVisible(false);
//            menuLogout.setVisible(true);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        NavigationView navigationView = findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open_nav,R.string.open_nav);
        drawerLayout.addDrawerListener(toggle);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.actionProfile:
//                        Toast.makeText(UserMainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                return false;
//            }
//        });

        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new profile()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        replaceFragment(new userHome());

        bottomNavigationView.setBackground(null);

        // Bottom_items

        getSharedPref();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new userHome());
                    break;

                case R.id.committee:
                    replaceFragment(new usercommittee());
                    break;

            }
            return true;
        });




        // drawer_items

        navigationView.setNavigationItemSelectedListener(item -> {

                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {

                    replaceFragment(new userHome());

                } else if (itemId == R.id.nav_share) {

                    replaceFragment(new share());

                   // Toast.makeText(UserMainActivity.this, " Share   ", Toast.LENGTH_SHORT).show();

                    drawerLayout.closeDrawer(GravityCompat.START);


                } else if (itemId == R.id.nav_about) {

                    replaceFragment(new about());

                    drawerLayout.closeDrawer(GravityCompat.START);
                    //loadFragment(new AboutUs());

                } else if(itemId==R.id.nav_login) {
                    // login
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent= new Intent(getApplicationContext(),log_in.class);
                    startActivity(intent);
                }
                else if (itemId==R.id.nav_logout) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
                    builder.setMessage("Are you sure....?");
                    builder.setTitle("Do you want to Logout");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        replaceFragment(new userHome());

//                        finish();
                    });

                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }


            drawerLayout.closeDrawer(GravityCompat.START);

                return false;
        });


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBottomDialog();
//            }
//        });
    }

    // Method for load fragment


//
//    private void loadFragment(Fragment fragment) {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragment_layout, fragment);
//        ft.commit();
//    }


    //outside oncreate

    public void onBackPressed() {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

//    private void showBottomDialog() {
//
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.bottomsheetlayout);
//
//        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
//        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
//        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
//        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
//
//        videoLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        shortsLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        liveLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//
//    }

}