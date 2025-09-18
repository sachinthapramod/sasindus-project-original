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

import java.util.List;

public class AdminReservationAdapter extends RecyclerView.Adapter<AdminReservationAdapter.AdminReservationViewHolder> {

    private List<Reservation> reservations;
    private OnReservationActionListener listener;

    public interface OnReservationActionListener {
        void onConfirmReservation(Reservation reservation);
        void onDeleteReservation(Reservation reservation);
    }

    public AdminReservationAdapter(OnReservationActionListener listener) {
        this.listener = listener;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_reservation, parent, false);
        return new AdminReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservations != null ? reservations.size() : 0;
    }

    class AdminReservationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserId, tvServiceId, tvDate, tvStatus, tvConfirmationStatus;
        private ImageButton btnConfirm, btnDelete;

        public AdminReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvServiceId = itemView.findViewById(R.id.tvServiceId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvConfirmationStatus = itemView.findViewById(R.id.tvConfirmationStatus);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onConfirmReservation(reservations.get(position));
                        }
                    }
                }
            });

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
            tvUserId.setText("User ID: " + reservation.getUserId());
            tvServiceId.setText("Service ID: " + reservation.getServiceId());
            tvDate.setText("Date: " + reservation.getDate());
            tvStatus.setText("Status: " + reservation.getStatus());
            
            if (reservation.isConfirmed()) {
                tvConfirmationStatus.setText("Confirmed");
                tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                btnConfirm.setVisibility(View.GONE);
            } else {
                tvConfirmationStatus.setText("Pending Confirmation");
                tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
                btnConfirm.setVisibility(View.VISIBLE);
            }
        }
    }
}
