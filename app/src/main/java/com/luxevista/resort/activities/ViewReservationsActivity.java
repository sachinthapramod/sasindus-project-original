package com.luxevista.resort.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.AdminReservationAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Reservation;

import java.util.List;

public class ViewReservationsActivity extends AppCompatActivity implements AdminReservationAdapter.OnReservationActionListener {
    
    private RecyclerView recyclerViewReservations;
    private AdminReservationAdapter reservationAdapter;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        loadAllReservations();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        btnBack = findViewById(R.id.btnBack);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void setupRecyclerView() {
        reservationAdapter = new AdminReservationAdapter(this);
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReservations.setAdapter(reservationAdapter);
    }
    
    private void loadAllReservations() {
        List<Reservation> reservations = databaseHelper.getAllReservations();
        reservationAdapter.setReservations(reservations);
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
    public void onConfirmReservation(Reservation reservation) {
        if (reservation.isConfirmed()) {
            Toast.makeText(this, "Reservation is already confirmed", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Confirm Reservation")
                .setMessage("Are you sure you want to confirm this reservation? Once confirmed, users cannot edit or delete it.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseHelper.confirmReservation(reservation.getId())) {
                            Toast.makeText(ViewReservationsActivity.this, "Reservation confirmed successfully", Toast.LENGTH_SHORT).show();
                            loadAllReservations();
                        } else {
                            Toast.makeText(ViewReservationsActivity.this, "Failed to confirm reservation", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    @Override
    public void onDeleteReservation(Reservation reservation) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Reservation")
                .setMessage("Are you sure you want to delete this reservation?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseHelper.deleteReservation(reservation.getId())) {
                            Toast.makeText(ViewReservationsActivity.this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                            loadAllReservations();
                        } else {
                            Toast.makeText(ViewReservationsActivity.this, "Failed to delete reservation", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
