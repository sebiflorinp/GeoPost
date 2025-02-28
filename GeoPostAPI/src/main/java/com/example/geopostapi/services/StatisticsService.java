package com.example.geopostapi.services;

import com.example.geopostapi.dtos.CountryStatistic;
import com.example.geopostapi.dtos.CountyStatistic;
import com.example.geopostapi.dtos.FilterStatistic;
import com.example.geopostapi.models.Post;
import com.example.geopostapi.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private PostsRepository postsRepository;
    
    @Autowired
    public StatisticsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }
    
    public Object getNumberOfPostsPerCountry() {
        return postsRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(Post::getCountry, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new CountryStatistic(entry.getKey(), Math.toIntExact(entry.getValue())))
                .sorted(Comparator.comparing(CountryStatistic::getPosts).reversed()
                        .thenComparing(CountryStatistic::getCountry))
                .toList();
    }
    
    public Object getNumberOfPostsPerCounty() {
        return postsRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(Post::getCounty, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new CountyStatistic(entry.getKey(), Math.toIntExact(entry.getValue())))
                .sorted(Comparator.comparing(CountyStatistic::getPosts).reversed()
                        .thenComparing(CountyStatistic::getCounty))
                .toList();
    }
    
    public Object getNumberOfPostsPerFilter() {
        List<Post> posts = postsRepository.findAll();
        int todayPosts = posts.stream().filter(post -> post.getCreatedAt().equals(LocalDate.now())).toList().size();
        int last7DaysPosts = posts.stream().filter(post -> post.getCreatedAt().isAfter(LocalDate.now().minusDays(7))).toList().size();
        int last31DaysPosts = posts.stream().filter(post -> post.getCreatedAt().isAfter(LocalDate.now().minusDays(31))).toList().size();
        int last12MonthsPosts = posts.stream().filter(post -> post.getCreatedAt().isAfter(LocalDate.now().minusMonths(12))).toList().size();
        
        return List.of(
                new FilterStatistic("today", todayPosts),
                new FilterStatistic("last7days", last7DaysPosts),
                new FilterStatistic("last31days", last31DaysPosts),
                new FilterStatistic("last12months", last12MonthsPosts)
        );
    }
    
}
