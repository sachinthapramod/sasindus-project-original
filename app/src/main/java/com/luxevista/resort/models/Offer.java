package com.luxevista.resort.models;

public class Offer {
    private int id;
    private String title;
    private String description;
    private String validUntil;
    
    public Offer() {}
    
    public Offer(int id, String title, String description, String validUntil) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.validUntil = validUntil;
    }
    
    public Offer(String title, String description, String validUntil) {
        this.title = title;
        this.description = description;
        this.validUntil = validUntil;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getValidUntil() {
        return validUntil;
    }
    
    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }
}
