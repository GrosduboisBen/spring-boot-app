package com.example.demo;

import com.example.demo.model.Provider;
import com.example.demo.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProviderRepository providerRepository;

    @BeforeEach
    void setUp() {
        providerRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchProvider() throws Exception {
        String json = """
                {
                  "name": "Dev Experts",
                  "email": "contact@devexperts.com",
                  "contact": "987654321",
                  "category": "DEVELOPMENT"
                }
                """;

        // Create provider
        mockMvc.perform(post("/api/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dev Experts"));

        // Verify repository
        List<Provider> providers = providerRepository.findAll();
        assertThat(providers).hasSize(1);

        // Fetch via GET
        mockMvc.perform(get("/api/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("DEVELOPMENT"));
    }
}
