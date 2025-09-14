package com.luxevista.resort.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.RoomAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Booking;
import com.luxevista.resort.models.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RoomBookingActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewRooms;
    private RoomAdapter roomAdapter;
    private EditText etCheckinDate, etCheckoutDate;
    private Button btnBookRoom;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private List<Room> availableRooms;
    private Room selectedRoom;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking);
        
        initializeViews();
        initializeDatabase();
        initializeSharedPreferences();
        initializeCalendar();
        setupRecyclerView();
        loadAvailableRooms();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        etCheckinDate = findViewById(R.id.etCheckinDate);
        etCheckoutDate = findViewById(R.id.etCheckoutDate);
        btnBookRoom = findViewById(R.id.btnBookRoom);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
    }
    
    private void initializeCalendar() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }
    
    private void setupRecyclerView() {
        availableRooms = new ArrayList<>();
        roomAdapter = new RoomAdapter(availableRooms, new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onRoomClick(Room room) {
                selectedRoom = room;
                Toast.makeText(RoomBookingActivity.this, "Selected: " + room.getRoomType(), Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRooms.setAdapter(roomAdapter);
    }
    
    private void loadAvailableRooms() {
        List<Room> allRooms = databaseHelper.getAllRooms();
        availableRooms.clear();
        
        for (Room room : allRooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        
        roomAdapter.notifyDataSetChanged();
    }
    
    private void setupClickListeners() {
        btnBookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookRoom();
            }
        });
        
        etCheckinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etCheckinDate, "Select Check-in Date");
            }
        });
        
        etCheckoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etCheckoutDate, "Select Check-out Date");
            }
        });
    }
    
    private void bookRoom() {
        String checkinDate = etCheckinDate.getText().toString().trim();
        String checkoutDate = etCheckoutDate.getText().toString().trim();
        
        if (validateBookingInput(checkinDate, checkoutDate)) {
            if (selectedRoom == null) {
                Toast.makeText(this, "Please select a room", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int userId = sharedPreferences.getInt("user_id", -1);
            Booking booking = new Booking(userId, selectedRoom.getId(), checkinDate, checkoutDate, "confirmed");
            
            if (databaseHelper.addBooking(booking)) {
                Toast.makeText(this, "Room booked successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private boolean validateBookingInput(String checkinDate, String checkoutDate) {
        boolean isValid = true;
        
        if (TextUtils.isEmpty(checkinDate)) {
            etCheckinDate.setError("Check-in date is required");
            isValid = false;
        }
        
        if (TextUtils.isEmpty(checkoutDate)) {
            etCheckoutDate.setError("Check-out date is required");
            isValid = false;
        }
        
        if (!TextUtils.isEmpty(checkinDate) && !TextUtils.isEmpty(checkoutDate)) {
            if (checkoutDate.compareTo(checkinDate) <= 0) {
                etCheckoutDate.setError("Check-out date must be after check-in date");
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    private void showDatePickerDialog(EditText editText, String title) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editText.setText(dateFormat.format(calendar.getTime()));
            }
        };
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        
        datePickerDialog.setTitle(title);
        datePickerDialog.show();
    }
}
