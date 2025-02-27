package com.example.geopostapi.controllers;

import com.example.geopostapi.dtos.PostDTO;
import com.example.geopostapi.models.Post;
import com.example.geopostapi.services.PostsService;
import org.hibernate.query.NativeQuery;
import org.hibernate.sql.ast.tree.insert.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostsController {
    private static final Logger log = LoggerFactory.getLogger(PostsController.class);
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
    
    @GetMapping("/posts")
    public List<Post> getAllPosts(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "county", required = false) String county
    ) {
        return postsService.getAllPosts(filter, country, county);
    }
    
}
