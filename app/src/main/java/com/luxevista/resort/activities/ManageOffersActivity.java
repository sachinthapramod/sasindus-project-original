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
import com.luxevista.resort.adapters.OfferAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Offer;

import java.util.List;

public class ManageOffersActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewOffers;
    private OfferAdapter offerAdapter;
    private EditText etOfferTitle, etOfferDescription, etValidUntil;
    private Button btnAddOffer, btnBack;
    private DatabaseHelper databaseHelper;
    private List<Offer> offers;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_offers);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        loadOffers();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewOffers = findViewById(R.id.recyclerViewOffers);
        etOfferTitle = findViewById(R.id.etOfferTitle);
        etOfferDescription = findViewById(R.id.etOfferDescription);
        etValidUntil = findViewById(R.id.etValidUntil);
        btnAddOffer = findViewById(R.id.btnAddOffer);
        btnBack = findViewById(R.id.btnBack);
    }
    
    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void setupRecyclerView() {
        offerAdapter = new OfferAdapter();
        recyclerViewOffers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOffers.setAdapter(offerAdapter);
    }
    
    private void loadOffers() {
        offers = databaseHelper.getAllOffers();
        offerAdapter.setOffers(offers);
    }
    
    private void setupClickListeners() {
        btnAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOffer();
            }
        });
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void addOffer() {
        String offerTitle = etOfferTitle.getText().toString().trim();
        String offerDescription = etOfferDescription.getText().toString().trim();
        String validUntil = etValidUntil.getText().toString().trim();
        
        if (validateOfferInput(offerTitle, validUntil)) {
            Offer offer = new Offer(offerTitle, offerDescription, validUntil);
            
            if (databaseHelper.addOffer(offer)) {
                Toast.makeText(this, "Offer added successfully!", Toast.LENGTH_SHORT).show();
                etOfferTitle.setText("");
                etOfferDescription.setText("");
                etValidUntil.setText("");
                loadOffers();
            } else {
                Toast.makeText(this, "Failed to add offer. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private boolean validateOfferInput(String offerTitle, String validUntil) {
        boolean isValid = true;
        
        if (TextUtils.isEmpty(offerTitle)) {
            etOfferTitle.setError("Offer title is required");
            isValid = false;
        }
        
        if (TextUtils.isEmpty(validUntil)) {
            etValidUntil.setError("Valid until date is required");
            isValid = false;
        }
        
        return isValid;
    }
}
