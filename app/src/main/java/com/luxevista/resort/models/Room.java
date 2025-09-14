package com.luxevista.resort.models;

public class Room {
    private int id;
    private String roomType;
    private String description;
    private String imagePath;
    private double price;
    private int availability;
    
    public Room() {}
    
    public Room(int id, String roomType, String description, String imagePath, double price, int availability) {
        this.id = id;
        this.roomType = roomType;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.availability = availability;
    }
    
    public Room(String roomType, String description, String imagePath, double price, int availability) {
        this.roomType = roomType;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.availability = availability;
    }
    
    // Legacy constructor for backward compatibility
    public Room(int id, String roomType, double price, int availability) {
        this.id = id;
        this.roomType = roomType;
        this.description = "";
        this.imagePath = "";
        this.price = price;
        this.availability = availability;
    }
    
    public Room(String roomType, double price, int availability) {
        this.roomType = roomType;
        this.description = "";
        this.imagePath = "";
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
