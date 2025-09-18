package com.luxevista.resort.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.BookingAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Booking;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements BookingAdapter.OnBookingActionListener {
    
    private TextView tvUserName, tvUserEmail, tvNoBookings;
    private RecyclerView recyclerViewBookings;
    private BookingAdapter bookingAdapter;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_profile);
            
            initializeViews();
            initializeDatabase();
            initializeSharedPreferences();
            setupUserInfo();
            setupRecyclerView();
            setupClickListeners();
            // Load bookings after everything else is set up
            loadUserBookings();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish(); // Close the activity if there's an error
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh bookings when user returns to profile (e.g., after making a new booking)
        loadUserBookings();
    }
    
    private void initializeViews() {
        try {
            tvUserName = findViewById(R.id.tvUserName);
            tvUserEmail = findViewById(R.id.tvUserEmail);
            tvNoBookings = findViewById(R.id.tvNoBookings);
            recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
            btnBack = findViewById(R.id.btnBack);
            
            if (tvUserName == null || tvUserEmail == null || tvNoBookings == null || 
                recyclerViewBookings == null || btnBack == null) {
                throw new RuntimeException("One or more views not found in layout");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error finding views: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            throw e;
        }
    }
    
    private void initializeDatabase() {
        try {
            databaseHelper = new DatabaseHelper(this);
            // Test database connection
            if (databaseHelper == null) {
                throw new RuntimeException("Failed to initialize database helper");
            }
            
            // Ensure database schema is correct
            databaseHelper.ensureDatabaseSchema();
            
            // Clean up any existing test bookings
            databaseHelper.cleanupTestBookings();
            
            // Fix any inconsistent booking statuses
            databaseHelper.fixInconsistentBookingStatuses();
            
            // Check if specific email exists
            databaseHelper.checkEmailExists("sachinthaworks@gmail.com");
            
            // Show all users in database
            databaseHelper.showAllUsers();
            
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing database: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            throw e;
        }
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
        try {
            bookingAdapter = new BookingAdapter(this);
            bookingAdapter.setDatabaseHelper(databaseHelper);
            recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewBookings.setAdapter(bookingAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up bookings list: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void loadUserBookings() {
        try {
            int userId = sharedPreferences.getInt("user_id", -1);
            if (userId == -1) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }
            
            List<Booking> bookings = databaseHelper.getBookingsByUserId(userId);
            bookingAdapter.setBookings(bookings);
            
            // Show/hide no bookings message
            if (bookings.isEmpty()) {
                tvNoBookings.setVisibility(View.VISIBLE);
                recyclerViewBookings.setVisibility(View.GONE);
            } else {
                tvNoBookings.setVisibility(View.GONE);
                recyclerViewBookings.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading bookings: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    @Override
    public void onDeleteBooking(Booking booking) {
        if (booking.isConfirmed()) {
            Toast.makeText(this, "Cannot delete confirmed booking", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showDeleteConfirmationDialog(booking);
    }
    
    private void showDeleteConfirmationDialog(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Booking")
                .setMessage("Are you sure you want to delete this booking? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseHelper.deleteBooking(booking.getId())) {
                            Toast.makeText(ProfileActivity.this, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                            loadUserBookings(); // Refresh the list
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
}
