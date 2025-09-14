package com.luxevista.resort.models;

public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private String checkinDate;
    private String checkoutDate;
    private String status;
    
    public Booking() {}
    
    public Booking(int id, int userId, int roomId, String checkinDate, String checkoutDate, String status) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.status = status;
    }
    
    public Booking(int userId, int roomId, String checkinDate, String checkoutDate, String status) {
        this.userId = userId;
        this.roomId = roomId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
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
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public String getCheckinDate() {
        return checkinDate;
    }
    
    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }
    
    public String getCheckoutDate() {
        return checkoutDate;
    }
    
    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
