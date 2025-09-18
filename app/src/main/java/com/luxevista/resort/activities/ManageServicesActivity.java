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
import com.luxevista.resort.adapters.AdminServiceAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ManageServicesActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewServices;
    private AdminServiceAdapter serviceAdapter;
    private EditText etServiceName, etServiceDescription, etServicePrice;
    private ImageView ivServiceImage;
    private TextView tvServiceImagePath;
    private Button btnAddService, btnBack, btnSelectServiceImage;
    private DatabaseHelper databaseHelper;
    private List<Service> services;
    private Service editingService;
    private String selectedImagePath;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);
        
        initializeViews();
        initializeDatabase();
        setupImagePicker();
        setupRecyclerView();
        loadServices();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        etServiceName = findViewById(R.id.etServiceName);
        etServiceDescription = findViewById(R.id.etServiceDescription);
        etServicePrice = findViewById(R.id.etServicePrice);
        ivServiceImage = findViewById(R.id.ivServiceImage);
        tvServiceImagePath = findViewById(R.id.tvServiceImagePath);
        btnAddService = findViewById(R.id.btnAddService);
        btnBack = findViewById(R.id.btnBack);
        btnSelectServiceImage = findViewById(R.id.btnSelectServiceImage);
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
                                tvServiceImagePath.setText("Image selected successfully");
                            } else {
                                Toast.makeText(ManageServicesActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ManageServicesActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(ManageServicesActivity.this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManageServicesActivity.this, "Failed to select image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }
    
    private String copyImageToInternalStorage(Uri imageUri) {
        try {
            // Create images directory in internal storage
            File imagesDir = new File(getFilesDir(), "service_images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }
            
            // Generate unique filename
            String fileName = "service_" + System.currentTimeMillis() + ".jpg";
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
                    ivServiceImage.setImageBitmap(bitmap);
                } else {
                    ivServiceImage.setImageResource(R.drawable.spa);
                }
            } else {
                ivServiceImage.setImageResource(R.drawable.spa);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ivServiceImage.setImageResource(R.drawable.spa);
        }
    }
    
    private void loadImageFromUri(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ivServiceImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupRecyclerView() {
        serviceAdapter = new AdminServiceAdapter(services, new AdminServiceAdapter.OnServiceActionListener() {
            @Override
            public void onEditService(Service service) {
                editService(service);
            }
            
            @Override
            public void onDeleteService(Service service) {
                deleteService(service);
            }
        });
        
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewServices.setAdapter(serviceAdapter);
    }
    
    private void loadServices() {
        services = databaseHelper.getAllServices();
        serviceAdapter.setServices(services);
    }
    
    private void setupClickListeners() {
        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        btnSelectServiceImage.setOnClickListener(new View.OnClickListener() {
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
    
    private void addService() {
        String serviceName = etServiceName.getText().toString().trim();
        String serviceDescription = etServiceDescription.getText().toString().trim();
        String priceText = etServicePrice.getText().toString().trim();
        
        if (validateServiceInput(serviceName, priceText)) {
            double price = Double.parseDouble(priceText);
            
            if (editingService != null) {
                // Update existing service
                editingService.setServiceName(serviceName);
                editingService.setDescription(serviceDescription);
                editingService.setImagePath(selectedImagePath != null ? selectedImagePath : "");
                editingService.setPrice(price);
                
                if (databaseHelper.updateService(editingService)) {
                    Toast.makeText(this, "Service updated successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadServices();
                } else {
                    Toast.makeText(this, "Failed to update service. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add new service
                Service service = new Service(serviceName, serviceDescription, selectedImagePath != null ? selectedImagePath : "", price, 1);
                
                if (databaseHelper.addService(service)) {
                    Toast.makeText(this, "Service added successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadServices();
                } else {
                    Toast.makeText(this, "Failed to add service. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    private boolean validateServiceInput(String serviceName, String priceText) {
        boolean isValid = true;
        
        if (TextUtils.isEmpty(serviceName)) {
            etServiceName.setError("Service name is required");
            isValid = false;
        }
        
        if (TextUtils.isEmpty(priceText)) {
            etServicePrice.setError("Price is required");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceText);
                if (price <= 0) {
                    etServicePrice.setError("Price must be greater than 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etServicePrice.setError("Please enter a valid price");
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    private void editService(Service service) {
        editingService = service;
        etServiceName.setText(service.getServiceName());
        etServiceDescription.setText(service.getDescription());
        etServicePrice.setText(String.valueOf(service.getPrice()));
        selectedImagePath = service.getImagePath();
        
        if (!TextUtils.isEmpty(service.getImagePath())) {
            // Check if it's a file path (new format) or URI (old format)
            if (service.getImagePath().startsWith("/")) {
                // It's a file path
                loadImageFromPath(service.getImagePath());
                tvServiceImagePath.setText("Image loaded");
            } else {
                // It's a URI (old format)
                try {
                    Uri imageUri = Uri.parse(service.getImagePath());
                    loadImageFromUri(imageUri);
                    tvServiceImagePath.setText("Image loaded");
                } catch (Exception e) {
                    tvServiceImagePath.setText("Error loading image");
                    ivServiceImage.setImageResource(R.drawable.spa);
                }
            }
        } else {
            ivServiceImage.setImageResource(R.drawable.spa);
            tvServiceImagePath.setText("No image selected");
        }
        
        btnAddService.setText("Update Service");
    }
    
    private void deleteService(Service service) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Service")
            .setMessage("Are you sure you want to delete this service?")
            .setPositiveButton("Delete", (dialog, which) -> {
                if (databaseHelper.deleteService(service.getId())) {
                    Toast.makeText(this, "Service deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadServices();
                } else {
                    Toast.makeText(this, "Failed to delete service. Please try again.", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private void clearForm() {
        etServiceName.setText("");
        etServiceDescription.setText("");
        etServicePrice.setText("");
        ivServiceImage.setImageResource(R.drawable.spa);
        tvServiceImagePath.setText("No image selected");
        selectedImagePath = null;
        btnAddService.setText("Add Service");
        editingService = null;
    }
}
