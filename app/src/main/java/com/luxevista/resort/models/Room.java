package com.luxevista.resort.models;

public class Room {
    private int id;
    private String roomType;
    private double price;
    private int availability;
    
    public Room() {}
    
    public Room(int id, String roomType, double price, int availability) {
        this.id = id;
        this.roomType = roomType;
        this.price = price;
        this.availability = availability;
    }
    
    public Room(String roomType, double price, int availability) {
        this.roomType = roomType;
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
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
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
