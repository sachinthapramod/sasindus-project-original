package com.luxevista.resort.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.BookingAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Booking;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    
    private TextView tvUserName, tvUserEmail;
    private RecyclerView recyclerViewBookings;
    private BookingAdapter bookingAdapter;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        initializeViews();
        initializeDatabase();
        initializeSharedPreferences();
        setupUserInfo();
        setupRecyclerView();
        loadUserBookings();
        setupClickListeners();
    }
    
    private void initializeViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        btnBack = findViewById(R.id.btnBack);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
    }
    
    private void setupUserInfo() {
        String userName = sharedPreferences.getString("user_name", "");
        String userEmail = sharedPreferences.getString("user_email", "");
        
        tvUserName.setText("Name: " + userName);
        tvUserEmail.setText("Email: " + userEmail);
    }
    
    private void setupRecyclerView() {
        bookingAdapter = new BookingAdapter();
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(bookingAdapter);
    }
    
    private void loadUserBookings() {
        int userId = sharedPreferences.getInt("user_id", -1);
        List<Booking> bookings = databaseHelper.getBookingsByUserId(userId);
        bookingAdapter.setBookings(bookings);
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
