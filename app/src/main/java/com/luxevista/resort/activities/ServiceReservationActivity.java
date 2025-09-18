package com.luxevista.resort.activities;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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
import com.luxevista.resort.models.Reservation;
import com.luxevista.resort.models.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ServiceReservationActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewServices;
    private ServiceAdapter serviceAdapter;
    private EditText etReservationDate;
    private Button btnReserveService;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private List<Service> availableServices;
    private Service selectedService;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_reservation);
        
        initializeViews();
        initializeDatabase();
        initializeSharedPreferences();
        initializeCalendar();
        setupRecyclerView();
        loadAvailableServices();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        etReservationDate = findViewById(R.id.etReservationDate);
        btnReserveService = findViewById(R.id.btnReserveService);
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
        availableServices = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(availableServices, new ServiceAdapter.OnServiceClickListener() {
            @Override
            public void onServiceClick(Service service) {
                selectedService = service;
                Toast.makeText(ServiceReservationActivity.this, "Selected: " + service.getServiceName(), Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewServices.setAdapter(serviceAdapter);
    }
    
    private void loadAvailableServices() {
        List<Service> allServices = databaseHelper.getAllServices();
        availableServices.clear();
        
        for (Service service : allServices) {
            if (service.isAvailable()) {
                availableServices.add(service);
            }
        }
        
        serviceAdapter.notifyDataSetChanged();
    }
    
    private void setupClickListeners() {
        btnReserveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveService();
            }
        });
        
        etReservationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etReservationDate, "Select Reservation Date");
            }
        });
    }
    
    private void reserveService() {
        String reservationDate = etReservationDate.getText().toString().trim();
        
        if (validateReservationInput(reservationDate)) {
            if (selectedService == null) {
                Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int userId = sharedPreferences.getInt("user_id", -1);
            Reservation reservation = new Reservation(userId, selectedService.getId(), reservationDate, "pending");
            // Explicitly ensure the reservation is not confirmed
            reservation.setConfirmed(false);
            
            // Debug: Log reservation details before saving
            android.util.Log.d("ServiceReservationActivity", "Creating reservation - User ID: " + userId + 
                ", Service ID: " + selectedService.getId() + 
                ", Status: " + reservation.getStatus() + 
                ", Confirmed: " + reservation.isConfirmed());
            
            if (databaseHelper.addReservation(reservation)) {
                Toast.makeText(this, "Service reserved successfully! Status: Pending Admin Approval", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Reservation failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private boolean validateReservationInput(String reservationDate) {
        if (TextUtils.isEmpty(reservationDate)) {
            etReservationDate.setError("Reservation date is required");
            return false;
        }
        return true;
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
        
        // Apply custom styles to buttons after showing
        Button okButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
        Button cancelButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE);
        
        if (okButton != null) {
            okButton.setBackgroundResource(R.drawable.calendar_button_ok);
            okButton.setTextColor(getResources().getColor(android.R.color.white, null));
            okButton.setTextSize(16);
            okButton.setTypeface(null, android.graphics.Typeface.BOLD);
            okButton.setPadding(48, 24, 48, 24);
        }
        
        if (cancelButton != null) {
            cancelButton.setBackgroundResource(R.drawable.calendar_button_cancel);
            cancelButton.setTextColor(getResources().getColor(R.color.primary_color, null));
            cancelButton.setTextSize(16);
            cancelButton.setTypeface(null, android.graphics.Typeface.BOLD);
            cancelButton.setPadding(48, 24, 48, 24);
        }
    }
}
