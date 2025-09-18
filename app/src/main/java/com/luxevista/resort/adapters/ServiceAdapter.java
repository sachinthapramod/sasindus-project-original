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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

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
        private ImageView ivServiceImage;
        
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServiceDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            ivServiceImage = itemView.findViewById(R.id.ivServiceImage);
            
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
            
            // Load service image
            loadServiceImage(service);
        }
        
        private void loadServiceImage(Service service) {
            if (!TextUtils.isEmpty(service.getImagePath()) && !service.getImagePath().equals("default_service_image")) {
                try {
                    // Check if it's a file path (new format) or URI (old format)
                    if (service.getImagePath().startsWith("/")) {
                        // It's a file path - load directly from file
                        File imageFile = new File(service.getImagePath());
                        if (imageFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(service.getImagePath());
                            if (bitmap != null) {
                                ivServiceImage.setImageBitmap(bitmap);
                            } else {
                                ivServiceImage.setImageResource(R.drawable.spa);
                            }
                        } else {
                            ivServiceImage.setImageResource(R.drawable.spa);
                        }
                    } else {
                        // It's a URI (old format) - try to load from URI
                        Uri imageUri = Uri.parse(service.getImagePath());
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(itemView.getContext().getContentResolver(), imageUri);
                        if (bitmap != null) {
                            ivServiceImage.setImageBitmap(bitmap);
                        } else {
                            ivServiceImage.setImageResource(R.drawable.spa);
                        }
                    }
                } catch (Exception e) {
                    // If there's any error loading the image, use default image
                    ivServiceImage.setImageResource(R.drawable.spa);
                }
            } else {
                // Use default service image
                ivServiceImage.setImageResource(R.drawable.spa);
            }
        }
    }
}
