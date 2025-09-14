package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Service;

import java.util.List;

public class AdminServiceAdapter extends RecyclerView.Adapter<AdminServiceAdapter.AdminServiceViewHolder> {
    
    private List<Service> services;
    private OnServiceActionListener listener;
    
    public interface OnServiceActionListener {
        void onEditService(Service service);
        void onDeleteService(Service service);
    }
    
    public AdminServiceAdapter(List<Service> services, OnServiceActionListener listener) {
        this.services = services;
        this.listener = listener;
    }
    
    public void setServices(List<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public AdminServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_service, parent, false);
        return new AdminServiceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AdminServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.bind(service);
    }
    
    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }
    
    class AdminServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceName, tvServiceDescription, tvServicePrice, tvAvailability;
        private ImageButton btnEdit, btnDelete;
        
        public AdminServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServiceDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditService(services.get(position));
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
                            listener.onDeleteService(services.get(position));
                        }
                    }
                }
            });
        }
        
        public void bind(Service service) {
            tvServiceName.setText(service.getServiceName());
            tvServiceDescription.setText(service.getDescription());
            tvServicePrice.setText("$" + String.format("%.2f", service.getPrice()));
            tvAvailability.setText(service.isAvailable() ? "Available" : "Not Available");
        }
    }
}
