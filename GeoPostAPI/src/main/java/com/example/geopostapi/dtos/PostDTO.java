package com.example.geopostapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PostDTO {
    @NotBlank
    private String title;
    
    @NotBlank
    private String text;
    
    private List<String> imageUrls;
    
    @NotNull
    private double lat;
    
    @NotNull
    private double lon;

    public PostDTO(String title, String text, List<String> imageUrls, double lon, double lat) {
        this.title = title;
        this.text = text;
        this.imageUrls = imageUrls;
        this.lon = lon;
        this.lat = lat;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getText() {
        return text;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public double getLat() {
        return lat;
    }
    
    public double getLon() {
        return lon;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    public void setLat(double lat) {
        this.lat = lat;
    }
    
    public void setLon(double lon) {
        this.lon = lon;
    }
}
