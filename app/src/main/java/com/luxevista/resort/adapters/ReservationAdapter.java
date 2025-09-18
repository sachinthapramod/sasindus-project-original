package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Reservation;
import com.luxevista.resort.models.Service;
import com.luxevista.resort.database.DatabaseHelper;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    
    private List<Reservation> reservations;
    private DatabaseHelper databaseHelper;
    private OnReservationActionListener listener;
    
    public interface OnReservationActionListener {
        void onDeleteReservation(Reservation reservation);
    }
    
    public ReservationAdapter(OnReservationActionListener listener) {
        this.reservations = new java.util.ArrayList<>();
        this.listener = listener;
    }
    
    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_reservation, parent, false);
        return new ReservationViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.bind(reservation);
    }
    
    @Override
    public int getItemCount() {
        return reservations != null ? reservations.size() : 0;
    }
    
    class ReservationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvReservationId, tvServiceName, tvReservationDate, tvStatus, tvConfirmationStatus;
        private ImageButton btnDelete;
        
        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationId = itemView.findViewById(R.id.tvReservationId);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvReservationDate = itemView.findViewById(R.id.tvReservationDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvConfirmationStatus = itemView.findViewById(R.id.tvConfirmationStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteReservation(reservations.get(position));
                        }
                    }
                }
            });
        }
        
        public void bind(Reservation reservation) {
            try {
                if (reservation == null) {
                    android.util.Log.e("ReservationAdapter", "Reservation is null");
                    return;
                }
                
                // Check if all TextViews are properly initialized
                if (tvReservationId == null) {
                    android.util.Log.e("ReservationAdapter", "tvReservationId is null");
                    return;
                }
                if (tvServiceName == null) {
                    android.util.Log.e("ReservationAdapter", "tvServiceName is null");
                    return;
                }
                if (tvReservationDate == null) {
                    android.util.Log.e("ReservationAdapter", "tvReservationDate is null");
                    return;
                }
                if (tvStatus == null) {
                    android.util.Log.e("ReservationAdapter", "tvStatus is null");
                    return;
                }
                if (tvConfirmationStatus == null) {
                    android.util.Log.e("ReservationAdapter", "tvConfirmationStatus is null");
                    return;
                }
                if (btnDelete == null) {
                    android.util.Log.e("ReservationAdapter", "btnDelete is null");
                    return;
                }
                
                tvReservationId.setText("Reservation ID: " + reservation.getId());
                
                // Get service details from database
                String serviceName = "Service ID: " + reservation.getServiceId();
                if (databaseHelper != null) {
                    try {
                        Service service = databaseHelper.getServiceById(reservation.getServiceId());
                        if (service != null) {
                            serviceName = "Service: " + service.getServiceName();
                        }
                    } catch (Exception e) {
                        // If there's an error getting service details, just use the ID
                        serviceName = "Service ID: " + reservation.getServiceId();
                    }
                }
                tvServiceName.setText(serviceName);
                
                tvReservationDate.setText("Date: " + reservation.getDate());
                tvStatus.setText("Status: " + reservation.getStatus());
                
                // Set confirmation status and show/hide delete button
                boolean isConfirmed = reservation.isConfirmed();
                
                if (isConfirmed) {
                    tvConfirmationStatus.setText("Confirmed");
                    tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                    btnDelete.setVisibility(View.GONE);
                } else {
                    tvConfirmationStatus.setText("Pending");
                    tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
                    btnDelete.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                // Log the error but don't crash the app
                android.util.Log.e("ReservationAdapter", "Error binding reservation: " + e.getMessage(), e);
            }
        }
    }
}
