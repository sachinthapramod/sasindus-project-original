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
import com.luxevista.resort.models.Room;
import com.luxevista.resort.database.DatabaseHelper;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    
    private List<Booking> bookings;
    private DatabaseHelper databaseHelper;
    private OnBookingActionListener listener;
    
    public interface OnBookingActionListener {
        void onDeleteBooking(Booking booking);
    }
    
    public BookingAdapter(OnBookingActionListener listener) {
        this.bookings = new java.util.ArrayList<>();
        this.listener = listener;
    }
    
    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
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
        private TextView tvBookingId, tvRoomId, tvCheckinDate, tvCheckoutDate, tvStatus, tvConfirmationStatus;
        private ImageButton btnDelete;
        
        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvRoomId = itemView.findViewById(R.id.tvRoomId);
            tvCheckinDate = itemView.findViewById(R.id.tvCheckinDate);
            tvCheckoutDate = itemView.findViewById(R.id.tvCheckoutDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvConfirmationStatus = itemView.findViewById(R.id.tvConfirmationStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            
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
            try {
                if (booking == null) {
                    android.util.Log.e("BookingAdapter", "Booking is null");
                    return;
                }
                
                // Check if all TextViews are properly initialized
                if (tvBookingId == null) {
                    android.util.Log.e("BookingAdapter", "tvBookingId is null");
                    return;
                }
                if (tvRoomId == null) {
                    android.util.Log.e("BookingAdapter", "tvRoomId is null");
                    return;
                }
                if (tvCheckinDate == null) {
                    android.util.Log.e("BookingAdapter", "tvCheckinDate is null");
                    return;
                }
                if (tvCheckoutDate == null) {
                    android.util.Log.e("BookingAdapter", "tvCheckoutDate is null");
                    return;
                }
                if (tvStatus == null) {
                    android.util.Log.e("BookingAdapter", "tvStatus is null");
                    return;
                }
                if (tvConfirmationStatus == null) {
                    android.util.Log.e("BookingAdapter", "tvConfirmationStatus is null");
                    return;
                }
                if (btnDelete == null) {
                    android.util.Log.e("BookingAdapter", "btnDelete is null");
                    return;
                }
                
                tvBookingId.setText("Booking ID: " + booking.getId());
                
                // Get room details from database (with caching)
                String roomType = "Room ID: " + booking.getRoomId();
                if (databaseHelper != null) {
                    try {
                        // Use a more efficient query or cache room data
                        Room room = databaseHelper.getRoomById(booking.getRoomId());
                        if (room != null) {
                            roomType = "Room: " + room.getRoomType();
                        }
                    } catch (Exception e) {
                        // If there's an error getting room details, just use the ID
                        roomType = "Room ID: " + booking.getRoomId();
                    }
                }
                tvRoomId.setText(roomType);
                
                tvCheckinDate.setText("Check-in: " + booking.getCheckinDate());
                tvCheckoutDate.setText("Check-out: " + booking.getCheckoutDate());
                tvStatus.setText("Status: " + booking.getStatus());
                
                // Set confirmation status and show/hide delete button
                boolean isConfirmed = booking.isConfirmed();
                
                if (isConfirmed) {
                    tvConfirmationStatus.setText("Confirmed");
                    tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.white));
                    tvConfirmationStatus.setBackgroundResource(R.drawable.status_confirmed_background);
                    btnDelete.setVisibility(View.GONE);
                } else {
                    tvConfirmationStatus.setText("Pending");
                    tvConfirmationStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.white));
                    tvConfirmationStatus.setBackgroundResource(R.drawable.status_pending_background);
                    btnDelete.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                // Log the error but don't crash the app
                android.util.Log.e("BookingAdapter", "Error binding booking: " + e.getMessage(), e);
            }
        }
    }
}
