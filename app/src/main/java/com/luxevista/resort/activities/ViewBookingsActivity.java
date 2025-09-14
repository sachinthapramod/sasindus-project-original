package com.luxevista.resort.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.BookingAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Booking;

import java.util.List;

public class ViewBookingsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewBookings;
    private BookingAdapter bookingAdapter;
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
        bookingAdapter = new BookingAdapter();
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
}
