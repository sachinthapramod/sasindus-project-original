package com.luxevista.resort.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.luxevista.resort.R;

public class AdminDashboardActivity extends AppCompatActivity {
    
    private TextView tvWelcome;
    private Button btnManageRooms, btnManageServices, btnManageOffers, btnViewBookings, btnLogout;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        
        initializeViews();
        initializeSharedPreferences();
        setupWelcomeMessage();
        setupClickListeners();
    }
    
    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        btnManageRooms = findViewById(R.id.btnManageRooms);
        btnManageServices = findViewById(R.id.btnManageServices);
        btnManageOffers = findViewById(R.id.btnManageOffers);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnLogout = findViewById(R.id.btnLogout);
    }
    
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
    }
    
    private void setupWelcomeMessage() {
        String userName = sharedPreferences.getString("user_name", "Admin");
        tvWelcome.setText("Welcome, " + userName + "!");
    }
    
    private void setupClickListeners() {
        btnManageRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ManageRoomsActivity.class);
                startActivity(intent);
            }
        });
        
        btnManageServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ManageServicesActivity.class);
                startActivity(intent);
            }
        });
        
        btnManageOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ManageOffersActivity.class);
                startActivity(intent);
            }
        });
        
        btnViewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, ViewBookingsActivity.class);
                startActivity(intent);
            }
        });
        
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
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
