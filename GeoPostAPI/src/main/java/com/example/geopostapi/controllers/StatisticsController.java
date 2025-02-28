package com.example.geopostapi.controllers;

import com.example.geopostapi.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private StatisticsService statisticsService;
    
    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }
    
    @GetMapping("/countries")
    public Object getNumberOfPostsPerCountry() {
        return statisticsService.getNumberOfPostsPerCountry();
    }
    
    @GetMapping("/counties")
    public Object getNumberOfPostsPerCounty() {
        return statisticsService.getNumberOfPostsPerCounty();
    }
    
    @GetMapping("/filters")
    public Object getNumberOfPostsPerFilter() {
        return statisticsService.getNumberOfPostsPerFilter();
    }
    
}
