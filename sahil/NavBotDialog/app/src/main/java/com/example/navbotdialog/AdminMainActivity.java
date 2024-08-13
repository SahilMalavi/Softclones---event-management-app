package com.example.navbotdialog;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.navbotdialog.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class AdminMainActivity extends AppCompatActivity {

    //    FloatingActionButton fab ;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    ActivityMainBinding binding;
    public void getSharedPref() {

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_login);

        final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        final String credentials = sharedPreferences.getString("mobile", "");
        final String name = sharedPreferences.getString("name", "");
        Toast.makeText(this, "Welcome " + name, Toast.LENGTH_SHORT).show();

        if (name.isEmpty()) {
            menuItem.setVisible(true);

        } else {
            menuItem.setVisible(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.open_nav);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new team()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        replaceFragment(new edit());

        bottomNavigationView.setBackground(null);

        // Bottom_items

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new edit());
                    break;
                case R.id.shorts:
                    replaceFragment(new team());
                    break;
                case R.id.subscriptions:
                    replaceFragment(new track());
                    break;
            }

            return true;
        });

        getSharedPref();


        // drawer_items

        navigationView.setNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {

                replaceFragment(new home());


            } else if (itemId == R.id.nav_share) {

                replaceFragment(new share());


                drawerLayout.closeDrawer(GravityCompat.START);


            } else if (itemId == R.id.nav_about) {

                replaceFragment(new about());

                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (itemId == R.id.nav_login) {
                // login
                drawerLayout.closeDrawer(GravityCompat.START);

                Toast.makeText(getApplicationContext(), " Admin already logged in.  ", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_logout) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
                builder.setMessage("Are you sure....?");
                builder.setTitle("Do you want to Logout");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                    final SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    finish();
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

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }
}