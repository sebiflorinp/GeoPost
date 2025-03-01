package com.example.geopostapi.services;

import com.example.geopostapi.dtos.PostDTO;
import com.example.geopostapi.models.Post;
import com.example.geopostapi.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.reverse;

@Service
public class PostsService {
    private PostsRepository postsRepository;
    private RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }
    
    public Post savePost(PostDTO requestBody) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://nominatim.openstreetmap.org/reverse")
                .queryParam("lat", requestBody.getLat())
                .queryParam("lon", requestBody.getLon())
                .queryParam("format", "json")
                .queryParam("addressdetails", 1)
                .queryParam("accept-language", "en")
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> address = (Map<String, Object>) response.get("address");
        String country = (String) address.get("country");

        System.out.println(address);
        
        String countyOrAlternative = (String) 
                address.getOrDefault("state_district", 
                address.getOrDefault("county", 
                address.getOrDefault("city", 
                address.getOrDefault("state_district", "N/A"))));

        LocalDate currentDate = LocalDate.now();
        Post postToSave =  new Post(
                requestBody.getTitle(),
                requestBody.getText(),
                requestBody.getImageUrls(),
                currentDate,
                countyOrAlternative, country,
                requestBody.getLat(),
                requestBody.getLon()
        );
        
        return postsRepository.save(postToSave);
    }
    
    public Optional<Post> getPostById(int id) {
        return postsRepository.getPostById(id);
    }

    public Page<Post> getAllPosts(String filter, String country, String county, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postsRepository.findAll();
        
        // if all the arguments are null return all posts
        if (filter == null && country == null && county == null) {
            posts.stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed()).toList();
            return postsRepository.findAll(pageable);
        }
        
        // if filter is not null return all posts according to their date
        if (filter != null) {
            switch (filter) {
                case "today":
                    posts.removeIf(post -> !post.getCreatedAt().isEqual(LocalDate.now()));
                    break;
                case "last7days":
                    posts.removeIf(post -> post.getCreatedAt().isBefore(LocalDate.now().minusDays(7)));
                    break;
                case "last31days":
                    posts.removeIf(post -> post.getCreatedAt().isBefore(LocalDate.now().minusDays(31)));
                    break;
                case "last12months":
                    posts.removeIf(post -> post.getCreatedAt().isBefore(LocalDate.now().minusMonths(12)));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid filter value");
            }
        }
        
        // if country is not null return all posts according to their country
        if (country != null) {
            posts.removeIf(post -> !post.getCountry().equals(country));
        }
        
        // if county is not null return all posts according to their county
        if (county != null) {
            posts.removeIf(post -> !post.getCounty().equals(county));
        }
        
        posts.stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed()).toList();
        return new PageImpl<>(posts, pageable, posts.size());
    }
}
