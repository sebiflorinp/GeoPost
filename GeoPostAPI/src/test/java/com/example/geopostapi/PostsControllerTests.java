package com.example.geopostapi;

import com.example.geopostapi.models.Post;
import com.example.geopostapi.repositories.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class PostsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PostsRepository postsRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void addPosts() {
        postsRepository.saveAll(List.of(
                new Post("title1", "text1", List.of("url1"), LocalDate.now(), "Greater London", "United Kingdom", 51.5074, -0.1278),
                new Post("title2", "text2", List.of("url2"), LocalDate.now().minusDays(3), "Bucharest", "Romania", 44.4268, 26.1025),
                new Post("title3", "text3", List.of("url3"), LocalDate.now().minusMonths(1), "Dallas County", "United States", 32.7767, -96.7970),
                new Post("title4", "text4", List.of("url4"), LocalDate.now().minusMonths(4), "Osaka", "Japan", 34.6937, 135.5023),
                new Post("title5", "text5", List.of("url5"), LocalDate.now().minusYears(4), "Wrocław", "Poland", 51.5074, -0.1278)
        ));
    }
    
    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
        jdbcTemplate.execute("ALTER TABLE post ALTER COLUMN id RESTART WITH 1");
    }
    
    @Test
    public void postPost_WithValidBody_ShouldReturnNewPost() throws Exception {
        String postRequestBodyJSON = 
                """
                    {
                        "title": "title1",
                        "text": "text1",
                        "imageUrls": ["url1"],
                        "lat": 51.5074,
                        "lon": -0.1278
                    }
                """;
        
        String currentDate = LocalDate.now().toString();
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequestBodyJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.text").value("text1"))
                .andExpect(jsonPath("$.imageUrls").value("url1"))
                .andExpect(jsonPath("$.createdAt").value(currentDate))
                .andExpect(jsonPath("$.country").value("United Kingdom"))
                .andExpect(jsonPath("$.county").value("Greater London"))
                .andExpect(jsonPath("$.lat").value(51.5074))
                .andExpect(jsonPath("$.lon").value(-0.1278));
    }
    
    @Test
    public void postPost_WithoutOptionalParameters_ShouldReturnNewPost() throws Exception {
        String postRequestBodyJSON =
                """
                    {
                        "title": "title1",
                        "imageUrls": ["url1"],
                        "lat": 51.5074,
                        "lon": -0.1278
                    }
                """;

        String currentDate = LocalDate.now().toString();
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postRequestBodyJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.text").doesNotExist())
                .andExpect(jsonPath("$.imageUrls").value("url1"))
                .andExpect(jsonPath("$.createdAt").value(currentDate))
                .andExpect(jsonPath("$.country").value("United Kingdom"))
                .andExpect(jsonPath("$.county").value("Greater London"))
                .andExpect(jsonPath("$.lat").value(51.5074))
                .andExpect(jsonPath("$.lon").value(-0.1278));
    }

    @Test
    public void getPost_ById_ShouldReturnPost() throws Exception {
        int postId = 2;

        mockMvc.perform(get("/api/posts/{id}", postId))
                .andExpect(status().isOk());
    }

    @Test
    public void getPost_ByNonExistentId_ShouldReturn404() throws Exception {
        int nonExistentPostId = 999; 

        mockMvc.perform(get("/api/posts/{id}", nonExistentPostId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllPosts_ShouldReturnAllPosts() throws Exception {
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[2].id").value("3"))
                .andExpect(jsonPath("$[3].id").value("4"))
                .andExpect(jsonPath("$[4].id").value("5"));
    }

    @Test
    public void getAllPosts_WithDateFilters_ShouldReturnFilteredPosts() throws Exception {
        // Test for posts from today
        mockMvc.perform(get("/api/posts")
                        .param("filter", "today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"));

        // Test for posts from the last 7 days
        mockMvc.perform(get("/api/posts")
                        .param("filter", "last7days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));

        // Test for posts from the last 31 days
        mockMvc.perform(get("/api/posts")
                        .param("filter", "last31days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[2].id").value("3"));

        // Test for posts from the last 12 months
        mockMvc.perform(get("/api/posts")
                        .param("filter", "last12months"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[2].id").value("3"))
                .andExpect(jsonPath("$[3].id").value("4"));
    }

    @Test
    public void getAllPosts_WithInvalidFilter_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("filter", "invalidFilter"))
                .andExpect(status().isBadRequest());
    }
}
