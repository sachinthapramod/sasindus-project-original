package com.luxevista.resort.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.RoomAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Room;

import java.util.List;

public class ManageRoomsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewRooms;
    private RoomAdapter roomAdapter;
    private EditText etRoomType, etRoomPrice;
    private Button btnAddRoom, btnBack;
    private DatabaseHelper databaseHelper;
    private List<Room> rooms;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rooms);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        loadRooms();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        etRoomType = findViewById(R.id.etRoomType);
        etRoomPrice = findViewById(R.id.etRoomPrice);
        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnBack = findViewById(R.id.btnBack);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void setupRecyclerView() {
        roomAdapter = new RoomAdapter(rooms, new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onRoomClick(Room room) {
                // For admin, we can implement edit/delete functionality here
                Toast.makeText(ManageRoomsActivity.this, "Room: " + room.getRoomType(), Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRooms.setAdapter(roomAdapter);
    }
    
    private void loadRooms() {
        rooms = databaseHelper.getAllRooms();
        roomAdapter.setRooms(rooms);
    }
    
    private void setupClickListeners() {
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoom();
            }
        });
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void addRoom() {
        String roomType = etRoomType.getText().toString().trim();
        String priceText = etRoomPrice.getText().toString().trim();
        
        if (validateRoomInput(roomType, priceText)) {
            double price = Double.parseDouble(priceText);
            Room room = new Room(roomType, price, 1);
            
            if (databaseHelper.addRoom(room)) {
                Toast.makeText(this, "Room added successfully!", Toast.LENGTH_SHORT).show();
                etRoomType.setText("");
                etRoomPrice.setText("");
                loadRooms();
            } else {
                Toast.makeText(this, "Failed to add room. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private boolean validateRoomInput(String roomType, String priceText) {
        boolean isValid = true;
        
        if (TextUtils.isEmpty(roomType)) {
            etRoomType.setError("Room type is required");
            isValid = false;
        }
        
        if (TextUtils.isEmpty(priceText)) {
            etRoomPrice.setError("Price is required");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceText);
                if (price <= 0) {
                    etRoomPrice.setError("Price must be greater than 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etRoomPrice.setError("Please enter a valid price");
                isValid = false;
            }
        }
        
        return isValid;
    }
}
