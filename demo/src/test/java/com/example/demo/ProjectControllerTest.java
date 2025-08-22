package com.example.demo;

import com.example.demo.model.Company;
import com.example.demo.model.Project;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.ProjectRepository;
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
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Company savedCompany;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
        companyRepository.deleteAll();

        // Create a company for the project FK
        savedCompany = companyRepository.save(Company.builder()
                .name("OpenAI")
                .email("contact@openai.com")
                .contact("123456789")
                .industry("AI Research")
                .build());
    }

    @Test
    void shouldCreateAndFetchProject() throws Exception {
        String json = """
                {
                  "title": "AI Chatbot",
                  "description": "Develop an AI-powered chatbot",
                  "budget": 50000,
                  "company": {"id": %d}
                }
                """.formatted(savedCompany.getId());

        // Create project
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("AI Chatbot"));

        // Verify repository
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(1);

        // Fetch via GET
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].budget").value(50000));
    }
}
