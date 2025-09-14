package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    
    private List<Offer> offers;
    
    public OfferAdapter() {
        this.offers = new java.util.ArrayList<>();
    }
    
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
        return new OfferViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offers.get(position);
        holder.bind(offer);
    }
    
    @Override
    public int getItemCount() {
        return offers != null ? offers.size() : 0;
    }
    
    class OfferViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOfferTitle, tvOfferDescription, tvValidUntil;
        
        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOfferTitle = itemView.findViewById(R.id.tvOfferTitle);
            tvOfferDescription = itemView.findViewById(R.id.tvOfferDescription);
            tvValidUntil = itemView.findViewById(R.id.tvValidUntil);
        }
        
        public void bind(Offer offer) {
            tvOfferTitle.setText(offer.getTitle());
            tvOfferDescription.setText(offer.getDescription());
            tvValidUntil.setText("Valid until: " + offer.getValidUntil());
        }
    }
}
