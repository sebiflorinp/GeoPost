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

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class StatisticsControllerTests {
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
                new Post("title2", "text2", List.of("url2"), LocalDate.now().minusDays(6), "Greater London", "United Kingdom", 51.5074, -0.1278),
                new Post("title3", "text3", List.of("url3"), LocalDate.now().minusDays(30), "Dallas County", "United States", 32.7767, -96.7970),
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
    public void getPostsCountByCountry_ShouldReturnPostCounts() throws Exception {
        mockMvc.perform(get("/api/statistics/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].country").value("United Kingdom"))
                .andExpect(jsonPath("$[0].posts").value(2))
                .andExpect(jsonPath("$[1].country").value("Japan"))
                .andExpect(jsonPath("$[1].posts").value(1))
                .andExpect(jsonPath("$[2].country").value("Poland"))
                .andExpect(jsonPath("$[2].posts").value(1))
                .andExpect(jsonPath("$[3].country").value("United States"))
                .andExpect(jsonPath("$[3].posts").value(1));
    }
    
    @Test
    public void getPostsCountByCounty_ShouldReturnPostCounts() throws Exception {
        mockMvc.perform(get("/api/statistics/counties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].county").value("Greater London"))
                .andExpect(jsonPath("$[0].posts").value(2))
                .andExpect(jsonPath("$[1].county").value("Dallas County"))
                .andExpect(jsonPath("$[1].posts").value(1))
                .andExpect(jsonPath("$[2].county").value("Osaka"))
                .andExpect(jsonPath("$[2].posts").value(1));
    }
    
    @Test
    public void getPostsCountByDate_ShouldReturnPostCounts() throws Exception {
        mockMvc.perform(get("/api/statistics/filters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].filter").value("today"))
                .andExpect(jsonPath("$[0].posts").value(1))
                .andExpect(jsonPath("$[1].filter").value("last7days"))
                .andExpect(jsonPath("$[1].posts").value(2))
                .andExpect(jsonPath("$[2].filter").value("last31days"))
                .andExpect(jsonPath("$[2].posts").value(3))
                .andExpect(jsonPath("$[3].filter").value("last12months"))
                .andExpect(jsonPath("$[3].posts").value(4));
    }
}
