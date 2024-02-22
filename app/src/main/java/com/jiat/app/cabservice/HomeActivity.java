package com.jiat.app.cabservice;

import static com.jiat.app.cabservice.R.id.my_location;
import static com.jiat.app.cabservice.R.id.request_ride;
import static com.jiat.app.cabservice.R.id.side_nav_logout;
import static com.jiat.app.cabservice.R.id.tour;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener ,
        NavigationBarView.OnItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar materialToolbar;
    private BottomNavigationView bottomNavigationView;
    private GoogleMap myMap;
    TextView logout;
    FirebaseAuth firebaseAuth;
    private BroadcastReceiver mReceiver;
    Airplanmode airplanmode = new Airplanmode();


    @Override
    protected void onStart() {

        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplanmode, filter);
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplanmode);
        unregisterReceiver(mReceiver);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        materialToolbar = findViewById(R.id.materialtoolbar);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        firebaseAuth = FirebaseAuth.getInstance();
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(side_nav_logout);

        mReceiver = new Airplanmode();

        setSupportActionBar(materialToolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this );

        Fragment fragment = new map_fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment).commit();

        /*SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);*/
        Menu menu1 = bottomNavigationView.getMenu();
        MenuItem menuItem1 = menu1.findItem(tour);


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == side_nav_logout){
            firebaseAuth.signOut();
            Toast.makeText(HomeActivity.this, "User SignOut.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId()== request_ride){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new ProfileFragment()).commit();
        }
        if (item.getItemId()== tour){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new TourFragment()).commit();
        }
        if (item.getItemId()== my_location){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new map_fragment()).commit();
        }
        return true;
    }


}