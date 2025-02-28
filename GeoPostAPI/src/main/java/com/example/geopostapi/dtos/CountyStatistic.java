package com.example.geopostapi.dtos;

public class CountyStatistic {
    private String county;
    private int posts;
    
    public CountyStatistic(String county, int posts) {
        this.county = county;
        this.posts = posts;
    }
    
    public String getCounty() {
        return county;
    }
    
    public int getPosts() {
        return posts;
    }
    
    public void setCounty(String county) {
        this.county = county;
    }
    
    public void setPosts(int posts) {
        this.posts = posts;
    }
}
