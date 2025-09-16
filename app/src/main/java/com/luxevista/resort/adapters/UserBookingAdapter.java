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

public class UserBookingAdapter extends RecyclerView.Adapter<UserBookingAdapter.UserBookingViewHolder> {

    private List<Booking> bookings;
    private OnBookingActionListener listener;

    public interface OnBookingActionListener {
        void onEditBooking(Booking booking);
        void onDeleteBooking(Booking booking);
    }

    public UserBookingAdapter(OnBookingActionListener listener) {
        this.listener = listener;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_booking, parent, false);
        return new UserBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserBookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings != null ? bookings.size() : 0;
    }

    class UserBookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoomType, tvCheckinDate, tvCheckoutDate, tvStatus, tvConfirmationStatus;
        private ImageButton btnEdit, btnDelete;

        public UserBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvCheckinDate = itemView.findViewById(R.id.tvCheckinDate);
            tvCheckoutDate = itemView.findViewById(R.id.tvCheckoutDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvConfirmationStatus = itemView.findViewById(R.id.tvConfirmationStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditBooking(bookings.get(position));
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
            // For now, we'll show room ID. In a real app, you'd fetch room details
            tvRoomType.setText("Room ID: " + booking.getRoomId());
            tvCheckinDate.setText("Check-in: " + booking.getCheckinDate());
            tvCheckoutDate.setText("Check-out: " + booking.getCheckoutDate());
            tvStatus.setText("Status: " + booking.getStatus());
            
            if (booking.isConfirmed()) {
                tvConfirmationStatus.setText("Confirmed");
                tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                btnEdit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
            } else {
                tvConfirmationStatus.setText("Pending Confirmation");
                tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
        }
    }
}
