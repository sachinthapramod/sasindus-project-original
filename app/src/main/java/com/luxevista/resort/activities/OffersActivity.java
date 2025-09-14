package com.luxevista.resort.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.adapters.OfferAdapter;
import com.luxevista.resort.database.DatabaseHelper;
import com.luxevista.resort.models.Offer;

import java.util.List;

public class OffersActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewOffers;
    private OfferAdapter offerAdapter;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        loadOffers();
        setupClickListeners();
    }
    
    private void initializeViews() {
        recyclerViewOffers = findViewById(R.id.recyclerViewOffers);
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
        List<Offer> offers = databaseHelper.getAllOffers();
        offerAdapter.setOffers(offers);
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
