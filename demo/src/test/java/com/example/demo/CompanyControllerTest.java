package com.example.demo;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
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
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        companyRepository.deleteAll(); // clean before each test
    }

    @Test
    void shouldCreateAndFetchCompany() throws Exception {
        String json = """
                {
                  "name": "OpenAI",
                  "email": "contact@openai.com",
                  "contact": "123456789",
                  "industry": "AI Research"
                }
                """;

        // Create company
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("OpenAI"));

        // Verify repository
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(1);

        // Fetch via GET
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("contact@openai.com"));
    }
}
