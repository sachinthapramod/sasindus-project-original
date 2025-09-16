package com.luxevista.resort.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.luxevista.resort.R;

public class GuestDashboardActivity extends AppCompatActivity {
    
    private TextView tvWelcome;
    private LinearLayout btnBookRoom, btnReserveService, btnViewOffers, btnMyBookings, btnProfile, btnLogout;
    private BottomNavigationView bottomNav;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);
        
        initializeViews();
        initializeSharedPreferences();
        setupWelcomeMessage();
        setupClickListeners();
    }
    
    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        btnBookRoom = findViewById(R.id.btnBookRoom);
        btnReserveService = findViewById(R.id.btnReserveService);
        btnViewOffers = findViewById(R.id.btnViewOffers);
        btnMyBookings = findViewById(R.id.btnMyBookings);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        bottomNav = findViewById(R.id.bottomNav);
    }
    
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
    }
    
    private void setupWelcomeMessage() {
        String userName = sharedPreferences.getString("user_name", "Guest");
        tvWelcome.setText("Welcome, " + userName + "!");
    }
    
    private void setupClickListeners() {
        btnBookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, RoomBookingActivity.class);
                startActivity(intent);
            }
        });
        
        btnReserveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, ServiceReservationActivity.class);
                startActivity(intent);
            }
        });
        
        btnViewOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, OffersActivity.class);
                startActivity(intent);
            }
        });
        
        btnMyBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, MyBookingsActivity.class);
                startActivity(intent);
            }
        });
        
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestDashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_rooms) {
                startActivity(new Intent(GuestDashboardActivity.this, RoomBookingActivity.class));
                return true;
            } else if (id == R.id.nav_services) {
                startActivity(new Intent(GuestDashboardActivity.this, ServiceReservationActivity.class));
                return true;
            } else if (id == R.id.nav_offers) {
                startActivity(new Intent(GuestDashboardActivity.this, OffersActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(GuestDashboardActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
    
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
