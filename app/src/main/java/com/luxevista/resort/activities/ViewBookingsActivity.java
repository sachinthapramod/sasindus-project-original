package com.luxevista.resort.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.AdminBookingAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Booking;

import java.util.List;

public class ViewBookingsActivity extends AppCompatActivity implements AdminBookingAdapter.OnBookingActionListener {
    
    private RecyclerView recyclerViewBookings;
    private AdminBookingAdapter bookingAdapter;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        loadAllBookings();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        btnBack = findViewById(R.id.btnBack);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void setupRecyclerView() {
        bookingAdapter = new AdminBookingAdapter(this);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(bookingAdapter);
    }
    
    private void loadAllBookings() {
        List<Booking> bookings = databaseHelper.getAllBookings();
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
    
    @Override
    public void onConfirmBooking(Booking booking) {
        if (booking.isConfirmed()) {
            Toast.makeText(this, "Booking is already confirmed", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage("Are you sure you want to confirm this booking? Once confirmed, users cannot edit or delete it.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseHelper.confirmBooking(booking.getId())) {
                            Toast.makeText(ViewBookingsActivity.this, "Booking confirmed successfully! User will see the updated status in their profile.", Toast.LENGTH_LONG).show();
                            loadAllBookings(); // Refresh the admin view
                        } else {
                            Toast.makeText(ViewBookingsActivity.this, "Failed to confirm booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    @Override
    public void onDeleteBooking(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Booking")
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseHelper.deleteBooking(booking.getId())) {
                            Toast.makeText(ViewBookingsActivity.this, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                            loadAllBookings();
                        } else {
                            Toast.makeText(ViewBookingsActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
