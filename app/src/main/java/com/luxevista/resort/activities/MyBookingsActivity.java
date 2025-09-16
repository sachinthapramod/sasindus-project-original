package com.luxevista.resort.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.UserBookingAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Booking;
import com.luxevista.resort.models.Room;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyBookingsActivity extends AppCompatActivity implements UserBookingAdapter.OnBookingActionListener {
    
    private RecyclerView recyclerViewBookings;
    private UserBookingAdapter bookingAdapter;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private int currentUserId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        
        initializeViews();
        initializeDatabase();
        initializeSharedPreferences();
        setupRecyclerView();
        loadUserBookings();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        btnBack = findViewById(R.id.btnBack);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
    }
    
    private void setupRecyclerView() {
        bookingAdapter = new UserBookingAdapter(this);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(bookingAdapter);
    }
    
    private void loadUserBookings() {
        if (currentUserId != -1) {
            List<Booking> bookings = databaseHelper.getBookingsByUserId(currentUserId);
            bookingAdapter.setBookings(bookings);
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
    public void onEditBooking(Booking booking) {
        if (booking.isConfirmed()) {
            Toast.makeText(this, "Cannot edit confirmed booking", Toast.LENGTH_SHORT).show();
            return;
        }
        showEditBookingDialog(booking);
    }
    
    @Override
    public void onDeleteBooking(Booking booking) {
        if (booking.isConfirmed()) {
            Toast.makeText(this, "Cannot delete confirmed booking", Toast.LENGTH_SHORT).show();
            return;
        }
        showDeleteConfirmationDialog(booking);
    }
    
    private void showEditBookingDialog(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_booking, null);
        
        EditText etCheckinDate = dialogView.findViewById(R.id.etCheckinDate);
        EditText etCheckoutDate = dialogView.findViewById(R.id.etCheckoutDate);
        
        etCheckinDate.setText(booking.getCheckinDate());
        etCheckoutDate.setText(booking.getCheckoutDate());
        
        // Set up date pickers
        Calendar calendar = Calendar.getInstance();
        etCheckinDate.setOnClickListener(v -> showDatePickerDialog(etCheckinDate, "Select Check-in Date"));
        etCheckoutDate.setOnClickListener(v -> showDatePickerDialog(etCheckoutDate, "Select Check-out Date"));
        
        builder.setView(dialogView)
                .setTitle("Edit Booking")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCheckinDate = etCheckinDate.getText().toString().trim();
                        String newCheckoutDate = etCheckoutDate.getText().toString().trim();
                        
                        if (validateDates(newCheckinDate, newCheckoutDate)) {
                            booking.setCheckinDate(newCheckinDate);
                            booking.setCheckoutDate(newCheckoutDate);
                            
                            if (databaseHelper.updateBooking(booking)) {
                                Toast.makeText(MyBookingsActivity.this, "Booking updated successfully", Toast.LENGTH_SHORT).show();
                                loadUserBookings();
                            } else {
                                Toast.makeText(MyBookingsActivity.this, "Failed to update booking", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", null);
        
        builder.create().show();
    }
    
    private void showDeleteConfirmationDialog(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Booking")
                .setMessage("Are you sure you want to delete this booking?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseHelper.deleteBooking(booking.getId())) {
                            Toast.makeText(MyBookingsActivity.this, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                            loadUserBookings();
                        } else {
                            Toast.makeText(MyBookingsActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void showDatePickerDialog(EditText editText, String title) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editText.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth));
            }
        };
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setTitle(title);
        datePickerDialog.show();
    }
    
    private boolean validateDates(String checkinDate, String checkoutDate) {
        if (checkinDate.isEmpty() || checkoutDate.isEmpty()) {
            Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (checkinDate.compareTo(checkoutDate) >= 0) {
            Toast.makeText(this, "Check-out date must be after check-in date", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
}
