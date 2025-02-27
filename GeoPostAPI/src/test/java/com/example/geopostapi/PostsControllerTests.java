package com.example.geopostapi;

import com.example.geopostapi.repositories.PostsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        String expectedPostRequestResultJSON = String.format(
                """
                    {
                        "id": 1,
                        "title": "title1",
                        "text": "text1",
                        "imageUrls": ["url1"],
                        "createdAt": "%s",
                        "country": "United Kingdom",
                        "county": "Greater London",
                        "lat": 51.5074,
                        "lon": -0.1278
                    }
                """, currentDate);
        
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequestBodyJSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedPostRequestResultJSON, true));
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
        String expectedPostRequestResultJSON = String.format(
                """
                    {
                        "id": 1,
                        "title": "title1",
                        "text": null,
                        "imageUrls": ["url1"],
                        "createdAt": "%s",
                        "country": "United Kingdom",
                        "county": "Greater London",
                        "lat": 51.5074,
                        "lon": -0.1278
                    }
                """, currentDate);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postRequestBodyJSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedPostRequestResultJSON, true));
    }
}
