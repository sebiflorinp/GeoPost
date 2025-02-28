package com.example.geopostapi.dtos;

public class CountryStatistic {
    private String country;
    private int posts;
    
    public CountryStatistic(String country, int posts) {
        this.country = country;
        this.posts = posts;
    }
    
    public String getCountry() {
        return country;
    }
    
    public int getPosts() {
        return posts;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public void setPosts(int posts) {
        this.posts = posts;
    }
}
