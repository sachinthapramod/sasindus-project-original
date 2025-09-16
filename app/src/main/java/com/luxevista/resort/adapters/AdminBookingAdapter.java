package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Booking;

import java.util.List;

public class AdminBookingAdapter extends RecyclerView.Adapter<AdminBookingAdapter.AdminBookingViewHolder> {

    private List<Booking> bookings;
    private OnBookingActionListener listener;

    public interface OnBookingActionListener {
        void onConfirmBooking(Booking booking);
        void onDeleteBooking(Booking booking);
    }

    public AdminBookingAdapter(OnBookingActionListener listener) {
        this.listener = listener;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_booking, parent, false);
        return new AdminBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings != null ? bookings.size() : 0;
    }

    class AdminBookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserId, tvRoomId, tvCheckinDate, tvCheckoutDate, tvStatus, tvConfirmationStatus;
        private ImageButton btnConfirm, btnDelete;

        public AdminBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvRoomId = itemView.findViewById(R.id.tvRoomId);
            tvCheckinDate = itemView.findViewById(R.id.tvCheckinDate);
            tvCheckoutDate = itemView.findViewById(R.id.tvCheckoutDate);
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
                            listener.onConfirmBooking(bookings.get(position));
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
                            listener.onDeleteBooking(bookings.get(position));
                        }
                    }
                }
            });
        }

        public void bind(Booking booking) {
            tvUserId.setText("User ID: " + booking.getUserId());
            tvRoomId.setText("Room ID: " + booking.getRoomId());
            tvCheckinDate.setText("Check-in: " + booking.getCheckinDate());
            tvCheckoutDate.setText("Check-out: " + booking.getCheckoutDate());
            tvStatus.setText("Status: " + booking.getStatus());
            
            if (booking.isConfirmed()) {
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
