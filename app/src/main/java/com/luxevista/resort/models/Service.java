package com.luxevista.resort.models;

public class Service {
    private int id;
    private String serviceName;
    private String description;
    private double price;
    private int availability;
    
    public Service() {}
    
    public Service(int id, String serviceName, String description, double price, int availability) {
        this.id = id;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }
    
    public Service(String serviceName, String description, double price, int availability) {
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getAvailability() {
        return availability;
    }
    
    public void setAvailability(int availability) {
        this.availability = availability;
    }
    
    public boolean isAvailable() {
        return availability == 1;
    }
}
