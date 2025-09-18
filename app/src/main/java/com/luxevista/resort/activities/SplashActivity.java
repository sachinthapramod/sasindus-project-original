package com.luxevista.resort.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.luxevista.resort.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 seconds
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        // Delay and then navigate to appropriate activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToNextActivity();
            }
        }, SPLASH_DELAY);
    }

    private void navigateToNextActivity() {
        // Check if user is already logged in
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
        String userRole = sharedPreferences.getString("user_role", "");

        Intent intent;
        
        if (isLoggedIn) {
            // User is logged in, navigate to appropriate dashboard
            if ("admin".equals(userRole)) {
                intent = new Intent(SplashActivity.this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, GuestDashboardActivity.class);
            }
        } else {
            // User is not logged in, navigate to login
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish(); // Close splash activity
    }
}
