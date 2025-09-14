package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    
    private List<Booking> bookings;
    
    public BookingAdapter() {
        this.bookings = new java.util.ArrayList<>();
    }
    
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }
    
    @Override
    public int getItemCount() {
        return bookings != null ? bookings.size() : 0;
    }
    
    class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookingId, tvRoomId, tvCheckinDate, tvCheckoutDate, tvStatus;
        
        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvRoomId = itemView.findViewById(R.id.tvRoomId);
            tvCheckinDate = itemView.findViewById(R.id.tvCheckinDate);
            tvCheckoutDate = itemView.findViewById(R.id.tvCheckoutDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
        
        public void bind(Booking booking) {
            tvBookingId.setText("Booking ID: " + booking.getId());
            tvRoomId.setText("Room ID: " + booking.getRoomId());
            tvCheckinDate.setText("Check-in: " + booking.getCheckinDate());
            tvCheckoutDate.setText("Check-out: " + booking.getCheckoutDate());
            tvStatus.setText("Status: " + booking.getStatus());
        }
    }
}
