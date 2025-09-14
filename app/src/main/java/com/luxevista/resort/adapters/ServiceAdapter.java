package com.luxevista.resort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luxevista.resort.R;
import com.luxevista.resort.models.Service;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    
    private List<Service> services;
    private OnServiceClickListener listener;
    
    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }
    
    public ServiceAdapter(List<Service> services, OnServiceClickListener listener) {
        this.services = services;
        this.listener = listener;
    }
    
    public void setServices(List<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.bind(service);
    }
    
    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }
    
    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceName, tvServiceDescription, tvServicePrice, tvAvailability;
        
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServiceDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onServiceClick(services.get(position));
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
