package com.example.geopostapi.controllers;

import com.example.geopostapi.dtos.PostDTO;
import com.example.geopostapi.models.Post;
import com.example.geopostapi.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostsController {
    private PostsService postsService;
    
    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }
    
    @PostMapping("/api/posts")
    public Post savePost(@RequestBody PostDTO postDTO) {
        return postsService.savePost(postDTO);
    }
    
}
