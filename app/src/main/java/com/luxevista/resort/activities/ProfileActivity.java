package com.luxevista.resort.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.BookingAdapter;
import com.luxevista.resort.adapters.ReservationAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Booking;
import com.luxevista.resort.models.Reservation;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements BookingAdapter.OnBookingActionListener, ReservationAdapter.OnReservationActionListener {
    
    private TextView tvUserName, tvUserEmail, tvNoBookings, tvNoReservations;
    private RecyclerView recyclerViewBookings, recyclerViewReservations;
    private BookingAdapter bookingAdapter;
    private ReservationAdapter reservationAdapter;
    private Button btnBack, btnBookingsTab, btnReservationsTab;
    private LinearLayout layoutBookings, layoutReservations;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private boolean isBookingsTabSelected = true;
    
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
            // Load bookings and reservations after everything else is set up
            loadUserBookings();
            loadUserReservations();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish(); // Close the activity if there's an error
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Only refresh data if we're coming from another activity (not just tab switching)
        // This prevents unnecessary database queries when switching between tabs
        if (shouldRefreshData()) {
            loadUserBookings();
            loadUserReservations();
        }
    }
    
    private boolean shouldRefreshData() {
        // Add logic to determine if data should be refreshed
        // For now, always refresh to maintain data consistency
        return true;
    }
    
    private void initializeViews() {
        try {
            tvUserName = findViewById(R.id.tvUserName);
            tvUserEmail = findViewById(R.id.tvUserEmail);
            tvNoBookings = findViewById(R.id.tvNoBookings);
            tvNoReservations = findViewById(R.id.tvNoReservations);
            recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
            recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
            btnBack = findViewById(R.id.btnBack);
            btnBookingsTab = findViewById(R.id.btnBookingsTab);
            btnReservationsTab = findViewById(R.id.btnReservationsTab);
            layoutBookings = findViewById(R.id.layoutBookings);
            layoutReservations = findViewById(R.id.layoutReservations);
            
            if (tvUserName == null || tvUserEmail == null || tvNoBookings == null || tvNoReservations == null ||
                recyclerViewBookings == null || recyclerViewReservations == null || btnBack == null ||
                btnBookingsTab == null || btnReservationsTab == null || layoutBookings == null || layoutReservations == null) {
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
            // Setup bookings recycler view
            bookingAdapter = new BookingAdapter(this);
            bookingAdapter.setDatabaseHelper(databaseHelper);
            recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewBookings.setAdapter(bookingAdapter);
            
            // Setup reservations recycler view
            reservationAdapter = new ReservationAdapter(this);
            reservationAdapter.setDatabaseHelper(databaseHelper);
            recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewReservations.setAdapter(reservationAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up lists: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
    
    private void loadUserReservations() {
        try {
            int userId = sharedPreferences.getInt("user_id", -1);
            if (userId == -1) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }
            
            List<Reservation> reservations = databaseHelper.getReservationsByUserId(userId);
            reservationAdapter.setReservations(reservations);
            
            // Show/hide no reservations message
            if (reservations.isEmpty()) {
                tvNoReservations.setVisibility(View.VISIBLE);
                recyclerViewReservations.setVisibility(View.GONE);
            } else {
                tvNoReservations.setVisibility(View.GONE);
                recyclerViewReservations.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading reservations: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        
        btnBookingsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToBookingsTab();
            }
        });
        
        btnReservationsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToReservationsTab();
            }
        });
    }
    
    private void switchToBookingsTab() {
        isBookingsTabSelected = true;
        btnBookingsTab.setBackgroundResource(R.drawable.tab_button_selected);
        btnReservationsTab.setBackgroundResource(R.drawable.tab_button_unselected);
        layoutBookings.setVisibility(View.VISIBLE);
        layoutReservations.setVisibility(View.GONE);
    }
    
    private void switchToReservationsTab() {
        isBookingsTabSelected = false;
        btnBookingsTab.setBackgroundResource(R.drawable.tab_button_unselected);
        btnReservationsTab.setBackgroundResource(R.drawable.tab_button_selected);
        layoutBookings.setVisibility(View.GONE);
        layoutReservations.setVisibility(View.VISIBLE);
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
    
    @Override
    public void onDeleteReservation(Reservation reservation) {
        if (reservation.isConfirmed()) {
            Toast.makeText(this, "Cannot delete confirmed reservation", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showDeleteReservationConfirmationDialog(reservation);
    }
    
    private void showDeleteReservationConfirmationDialog(Reservation reservation) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Reservation")
                .setMessage("Are you sure you want to delete this reservation? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseHelper.deleteReservation(reservation.getId())) {
                            Toast.makeText(ProfileActivity.this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                            loadUserReservations(); // Refresh the list
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to delete reservation", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
}
