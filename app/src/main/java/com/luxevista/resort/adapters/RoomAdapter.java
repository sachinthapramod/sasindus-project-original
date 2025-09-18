package com.luxevista.resort.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

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
        private ImageView ivRoomImage;
        
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            tvRoomDescription = itemView.findViewById(R.id.tvRoomDescription);
            ivRoomImage = itemView.findViewById(R.id.ivRoomImage);
            
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
            
            // Set room description - use custom description if available, otherwise use default
            String description = !TextUtils.isEmpty(room.getDescription()) ? 
                room.getDescription() : getRoomDescription(room.getRoomType());
            tvRoomDescription.setText(description);
            
            // Load room image
            loadRoomImage(room);
        }
        
        private void loadRoomImage(Room room) {
            if (!TextUtils.isEmpty(room.getImagePath()) && !room.getImagePath().equals("default_room_image")) {
                try {
                    // Check if it's a file path (new format) or URI (old format)
                    if (room.getImagePath().startsWith("/")) {
                        // It's a file path - load directly from file
                        File imageFile = new File(room.getImagePath());
                        if (imageFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(room.getImagePath());
                            if (bitmap != null) {
                                ivRoomImage.setImageBitmap(bitmap);
                            } else {
                                ivRoomImage.setImageResource(R.drawable.room);
                            }
                        } else {
                            ivRoomImage.setImageResource(R.drawable.room);
                        }
                    } else {
                        // It's a URI (old format) - try to load from URI
                        Uri imageUri = Uri.parse(room.getImagePath());
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(itemView.getContext().getContentResolver(), imageUri);
                        if (bitmap != null) {
                            ivRoomImage.setImageBitmap(bitmap);
                        } else {
                            ivRoomImage.setImageResource(R.drawable.room);
                        }
                    }
                } catch (Exception e) {
                    // If there's any error loading the image, use default image
                    ivRoomImage.setImageResource(R.drawable.room);
                }
            } else {
                // Use default room image
                ivRoomImage.setImageResource(R.drawable.room);
            }
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
