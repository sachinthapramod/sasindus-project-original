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
import com.luxevista.resort.adapters.ServiceAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Service;

import java.util.List;

public class ManageServicesActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewServices;
    private ServiceAdapter serviceAdapter;
    private EditText etServiceName, etServiceDescription, etServicePrice;
    private Button btnAddService, btnBack;
    private DatabaseHelper databaseHelper;
    private List<Service> services;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        loadServices();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        etServiceName = findViewById(R.id.etServiceName);
        etServiceDescription = findViewById(R.id.etServiceDescription);
        etServicePrice = findViewById(R.id.etServicePrice);
        btnAddService = findViewById(R.id.btnAddService);
        btnBack = findViewById(R.id.btnBack);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void setupRecyclerView() {
        serviceAdapter = new ServiceAdapter(services, new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onServiceClick(Service service) {
                // For admin, we can implement edit/delete functionality here
                Toast.makeText(ManageServicesActivity.this, "Service: " + service.getServiceName(), Toast.LENGTH_SHORT).show();
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
    }
    
    private void addService() {
        String serviceName = etServiceName.getText().toString().trim();
        String serviceDescription = etServiceDescription.getText().toString().trim();
        String priceText = etServicePrice.getText().toString().trim();
        
        if (validateServiceInput(serviceName, priceText)) {
            double price = Double.parseDouble(priceText);
            Service service = new Service(serviceName, serviceDescription, price, 1);
            
            if (databaseHelper.addService(service)) {
                Toast.makeText(this, "Service added successfully!", Toast.LENGTH_SHORT).show();
                etServiceName.setText("");
                etServiceDescription.setText("");
                etServicePrice.setText("");
                loadServices();
            } else {
                Toast.makeText(this, "Failed to add service. Please try again.", Toast.LENGTH_SHORT).show();
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
}
