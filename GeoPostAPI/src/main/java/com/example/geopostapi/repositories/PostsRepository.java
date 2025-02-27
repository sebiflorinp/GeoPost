package com.example.geopostapi.repositories;

import com.example.geopostapi.models.Post;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PostsRepository extends ListCrudRepository<Post, Integer> {
    Post save(Post newPost);
    Optional<Post> getPostById(int id);
}
