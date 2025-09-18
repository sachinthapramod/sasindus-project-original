package com.luxevista.resort.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.AdminRoomAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Room;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ManageRoomsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewRooms;
    private AdminRoomAdapter roomAdapter;
    private EditText etRoomType, etRoomDescription, etRoomPrice;
    private ImageView ivRoomImage;
    private TextView tvImagePath;
    private Button btnAddRoom, btnBack, btnSelectImage;
    private DatabaseHelper databaseHelper;
    private List<Room> rooms;
    private Room editingRoom;
    private String selectedImagePath;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rooms);
        
        initializeViews();
        initializeDatabase();
        setupImagePicker();
        setupRecyclerView();
        loadRooms();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
        etRoomType = findViewById(R.id.etRoomType);
        etRoomDescription = findViewById(R.id.etRoomDescription);
        etRoomPrice = findViewById(R.id.etRoomPrice);
        ivRoomImage = findViewById(R.id.ivRoomImage);
        tvImagePath = findViewById(R.id.tvImagePath);
        btnAddRoom = findViewById(R.id.btnAddRoom);
        btnBack = findViewById(R.id.btnBack);
        btnSelectImage = findViewById(R.id.btnSelectImage);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            // Copy image to internal storage and get the file path
                            String savedImagePath = copyImageToInternalStorage(imageUri);
                            if (savedImagePath != null) {
                                selectedImagePath = savedImagePath;
                                loadImageFromPath(savedImagePath);
                                tvImagePath.setText("Image selected successfully");
                            } else {
                                Toast.makeText(ManageRoomsActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ManageRoomsActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(ManageRoomsActivity.this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManageRoomsActivity.this, "Failed to select image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }
    
    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            // Create images directory in internal storage
            File imagesDir = new File(getFilesDir(), "room_images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }
            
            // Generate unique filename
            String fileName = "room_" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(imagesDir, fileName);
            
            // Copy image from URI to internal storage
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            
            inputStream.close();
            outputStream.close();
            
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void loadImageFromPath(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (bitmap != null) {
                    ivRoomImage.setImageBitmap(bitmap);
                } else {
                    ivRoomImage.setImageResource(R.drawable.room);
                }
            } else {
                ivRoomImage.setImageResource(R.drawable.room);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ivRoomImage.setImageResource(R.drawable.room);
        }
    }
    
    private void loadImageFromUri(Uri imageUri) {
        try {
            // Check if we have permission to read the image
            if (checkStoragePermission()) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                if (bitmap != null) {
                    ivRoomImage.setImageBitmap(bitmap);
                    tvImagePath.setText("Image selected successfully");
                } else {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission required to load image", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Permission denied to access image", Toast.LENGTH_SHORT).show();
        }
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
        
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }
    
    private void openImagePicker() {
        // Check if we have permission to read external storage
        if (checkStoragePermission()) {
            launchImagePicker();
        } else {
            requestStoragePermission();
        }
    }
    
    private boolean checkStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+)
            return ContextCompat.checkSelfPermission(this, 
                android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            // Android 12 and below
            return ContextCompat.checkSelfPermission(this, 
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
    
    private void requestStoragePermission() {
        String[] permissions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+)
            permissions = new String[]{android.Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            // Android 12 and below
            permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }
    
    private void launchImagePicker() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error opening image picker: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            } else {
                Toast.makeText(this, "Permission denied. Cannot select images.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void addRoom() {
        String roomType = etRoomType.getText().toString().trim();
        String roomDescription = etRoomDescription.getText().toString().trim();
        String priceText = etRoomPrice.getText().toString().trim();
        
        if (validateRoomInput(roomType, priceText)) {
            double price = Double.parseDouble(priceText);
            
            if (editingRoom != null) {
                // Update existing room
                editingRoom.setRoomType(roomType);
                editingRoom.setDescription(roomDescription);
                editingRoom.setImagePath(selectedImagePath != null ? selectedImagePath : "");
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
                Room room = new Room(roomType, roomDescription, selectedImagePath != null ? selectedImagePath : "", price, 1);
                
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
        etRoomDescription.setText(room.getDescription());
        etRoomPrice.setText(String.valueOf(room.getPrice()));
        selectedImagePath = room.getImagePath();
        
        if (!TextUtils.isEmpty(room.getImagePath())) {
            // Check if it's a file path (new format) or URI (old format)
            if (room.getImagePath().startsWith("/")) {
                // It's a file path
                loadImageFromPath(room.getImagePath());
                tvImagePath.setText("Image loaded");
            } else {
                // It's a URI (old format)
                try {
                    Uri imageUri = Uri.parse(room.getImagePath());
                    loadImageFromUri(imageUri);
                    tvImagePath.setText("Image loaded");
                } catch (Exception e) {
                    tvImagePath.setText("Error loading image");
                    ivRoomImage.setImageResource(R.drawable.room);
                }
            }
        } else {
            ivRoomImage.setImageResource(R.drawable.room);
            tvImagePath.setText("No image selected");
        }
        
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
        etRoomDescription.setText("");
        etRoomPrice.setText("");
        ivRoomImage.setImageResource(R.drawable.room);
        tvImagePath.setText("No image selected");
        selectedImagePath = null;
        btnAddRoom.setText("Add Room");
        editingRoom = null;
    }
}
