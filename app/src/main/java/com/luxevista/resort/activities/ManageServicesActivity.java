package com.luxevista.resort.activities;

import android.app.AlertDialog;
import android.content.Intent;
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
                            selectedImagePath = imageUri.toString();
                            loadImageFromUri(imageUri);
                            tvServiceImagePath.setText("Image selected");
                        }
                    }
                }
            }
        );
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
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
            try {
                Uri imageUri = Uri.parse(service.getImagePath());
                loadImageFromUri(imageUri);
                tvServiceImagePath.setText("Image loaded");
            } catch (Exception e) {
                tvServiceImagePath.setText("Error loading image");
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
