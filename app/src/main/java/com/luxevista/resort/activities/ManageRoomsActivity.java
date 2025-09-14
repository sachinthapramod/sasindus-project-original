package com.luxevista.resort.activities;

import android.app.AlertDialog;
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
import com.luxevista.resort.adapters.AdminRoomAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Room;

import java.util.List;

public class ManageRoomsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewRooms;
    private AdminRoomAdapter roomAdapter;
    private EditText etRoomType, etRoomPrice;
    private Button btnAddRoom, btnBack;
    private DatabaseHelper databaseHelper;
    private List<Room> rooms;
    private Room editingRoom;
    
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
        roomAdapter = new AdminRoomAdapter(rooms, new AdminRoomAdapter.OnRoomActionListener() {
            @Override
            public void onEditRoom(Room room) {
                editRoom(room);
            }
            
            @Override
            public void onDeleteRoom(Room room) {
                deleteRoom(room);
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
            
            if (editingRoom != null) {
                // Update existing room
                editingRoom.setRoomType(roomType);
                editingRoom.setPrice(price);
                
                if (databaseHelper.updateRoom(editingRoom)) {
                    Toast.makeText(this, "Room updated successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadRooms();
                } else {
                    Toast.makeText(this, "Failed to update room. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add new room
                Room room = new Room(roomType, price, 1);
                
                if (databaseHelper.addRoom(room)) {
                    Toast.makeText(this, "Room added successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadRooms();
                } else {
                    Toast.makeText(this, "Failed to add room. Please try again.", Toast.LENGTH_SHORT).show();
                }
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
    
    private void editRoom(Room room) {
        editingRoom = room;
        etRoomType.setText(room.getRoomType());
        etRoomPrice.setText(String.valueOf(room.getPrice()));
        btnAddRoom.setText("Update Room");
    }
    
    private void deleteRoom(Room room) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Room")
            .setMessage("Are you sure you want to delete this room?")
            .setPositiveButton("Delete", (dialog, which) -> {
                if (databaseHelper.deleteRoom(room.getId())) {
                    Toast.makeText(this, "Room deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadRooms();
                } else {
                    Toast.makeText(this, "Failed to delete room. Please try again.", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private void clearForm() {
        etRoomType.setText("");
        etRoomPrice.setText("");
        btnAddRoom.setText("Add Room");
        editingRoom = null;
    }
}
