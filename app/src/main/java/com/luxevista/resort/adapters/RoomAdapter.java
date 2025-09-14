package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    
    private List<Room> rooms;
    private OnRoomClickListener listener;
    
    public interface OnRoomClickListener {
        void onRoomClick(Room room);
    }
    
    public RoomAdapter(List<Room> rooms, OnRoomClickListener listener) {
        this.rooms = rooms;
        this.listener = listener;
    }
    
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room);
    }
    
    @Override
    public int getItemCount() {
        return rooms != null ? rooms.size() : 0;
    }
    
    class RoomViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoomType, tvRoomPrice, tvAvailability, tvRoomDescription;
        
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            tvRoomDescription = itemView.findViewById(R.id.tvRoomDescription);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRoomClick(rooms.get(position));
                        }
                    }
                }
            });
        }
        
        public void bind(Room room) {
            tvRoomType.setText(room.getRoomType());
            tvRoomPrice.setText("$" + String.format("%.2f", room.getPrice()));
            tvAvailability.setText(room.isAvailable() ? "Available" : "Not Available");
            
            // Set room description based on room type
            String description = getRoomDescription(room.getRoomType());
            tvRoomDescription.setText(description);
        }
        
        private String getRoomDescription(String roomType) {
            switch (roomType.toLowerCase()) {
                case "deluxe":
                    return "Spacious deluxe room with premium amenities and city view";
                case "suite":
                    return "Luxurious suite with separate living area and premium services";
                case "standard":
                    return "Comfortable standard room with modern amenities";
                case "presidential":
                    return "Exclusive presidential suite with panoramic views and butler service";
                default:
                    return "Luxurious room with modern amenities and excellent service";
            }
        }
    }
}
