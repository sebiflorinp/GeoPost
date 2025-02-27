package com.example.geopostapi.controllers;

import com.example.geopostapi.dtos.PostDTO;
import com.example.geopostapi.models.Post;
import com.example.geopostapi.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostsController {
    private PostsService postsService;
    
    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }
    
    @PostMapping("/posts")
    public ResponseEntity<Post> savePost(@RequestBody PostDTO postDTO) {
        
        return ResponseEntity.of(Optional.of(postsService.savePost(postDTO)));
    }
    
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") int id) {
        Optional<Post> postToReturn = postsService.getPostById(id);
        if (postToReturn.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.of(postToReturn);
    }
    
}
