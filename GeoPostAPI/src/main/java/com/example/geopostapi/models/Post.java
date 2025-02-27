package com.example.geopostapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank
    private String title;
    
    private String text;
    
    @ElementCollection
    private List<String> imageUrls;
    
    @NotNull
    private LocalDate createdAt;
    
    @NotBlank
    private String country;
    
    @NotBlank
    private String county;
    
    @NotNull
    private double lat;
    
    @NotNull
    private double lon;

    public Post() {
    }

    public Post(String title, String text, List<String> imageUrls, LocalDate createdAt, String county, String country, double lat, double lon) {
        this.title = title;
        this.text = text;
        this.imageUrls = imageUrls;
        this.createdAt = createdAt;
        this.county = county;
        this.country = country;
        this.lat = lat;
        this.lon = lon;
    }
    
    public int getId() {
        return id;
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
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getCounty() {
        return county;
    }
    
    public double getLat() {
        return lat;
    }
    
    public double getLon() {
        return lon;
    }
    
    public void setId(int id) {
        this.id = id;
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
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public void setCounty(String county) {
        this.county = county;
    }
    
    public void setLat(double lat) {
        this.lat = lat;
    }
    
    public void setLon(double lon) {
        this.lon = lon;
    }
}
