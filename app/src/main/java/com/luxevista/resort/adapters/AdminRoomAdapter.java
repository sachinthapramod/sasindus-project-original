package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Room;

import java.util.List;

public class AdminRoomAdapter extends RecyclerView.Adapter<AdminRoomAdapter.AdminRoomViewHolder> {
    
    private List<Room> rooms;
    private OnRoomActionListener listener;
    
    public interface OnRoomActionListener {
        void onEditRoom(Room room);
        void onDeleteRoom(Room room);
    }
    
    public AdminRoomAdapter(List<Room> rooms, OnRoomActionListener listener) {
        this.rooms = rooms;
        this.listener = listener;
    }
    
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public AdminRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_room, parent, false);
        return new AdminRoomViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AdminRoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room);
    }
    
    @Override
    public int getItemCount() {
        return rooms != null ? rooms.size() : 0;
    }
    
    class AdminRoomViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoomType, tvRoomDescription, tvRoomPrice, tvAvailability;
        private ImageButton btnEdit, btnDelete;
        
        public AdminRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomDescription = itemView.findViewById(R.id.tvRoomDescription);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditRoom(rooms.get(position));
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
                            listener.onDeleteRoom(rooms.get(position));
                        }
                    }
                }
            });
        }
        
        public void bind(Room room) {
            tvRoomType.setText(room.getRoomType());
            tvRoomDescription.setText(room.getDescription());
            tvRoomPrice.setText("$" + String.format("%.2f", room.getPrice()));
            tvAvailability.setText(room.isAvailable() ? "Available" : "Not Available");
        }
    }
}
