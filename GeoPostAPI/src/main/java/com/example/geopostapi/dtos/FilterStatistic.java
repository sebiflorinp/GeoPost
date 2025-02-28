package com.example.geopostapi.dtos;

public class FilterStatistic {
    private String filter;
    private int posts;
    
    public FilterStatistic(String filter, int posts) {
        this.filter = filter;
        this.posts = posts;
    }
    
    public String getFilter() {
        return filter;
    }
    
    public int getPosts() {
        return posts;
    }
    
    public void setFilter(String filter) {
        this.filter = filter;
    }
    
    public void setPosts(int posts) {
        this.posts = posts;
    }
}
