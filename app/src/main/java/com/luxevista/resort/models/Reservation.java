package com.luxevista.resort.models;

public class Reservation {
    private int id;
    private int userId;
    private int serviceId;
    private String date;
    private String status;
    
    public Reservation() {}
    
    public Reservation(int id, int userId, int serviceId, String date, String status) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.date = date;
        this.status = status;
    }
    
    public Reservation(int userId, int serviceId, String date, String status) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.date = date;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
