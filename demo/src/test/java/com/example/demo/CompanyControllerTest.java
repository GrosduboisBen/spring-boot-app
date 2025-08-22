package com.example.demo;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.EvaluationRepository;
import com.example.demo.repository.MissionRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.ProviderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ProviderRepository providerRepository;
    
    private Company company;
    @BeforeEach
    void setUp() {
        // Delete all children first
        evaluationRepository.deleteAll();
        missionRepository.deleteAll();
        orderRepository.deleteAll();
        projectRepository.deleteAll();
        providerRepository.deleteAll();
        companyRepository.deleteAll();

        // Create base company
        company = companyRepository.save(Company.builder()
                .name("Test Company")
                .email("test@company.com")
                .contact("123456")
                .industry("IT")
                .build());
    }

    @Test
    void testGetAllCompanies() throws Exception {
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testCreateCompany() throws Exception {
        Company newCompany = Company.builder()
                .name("Beta LLC")
                .email("info@beta.com")
                .contact("Alice")
                .industry("Marketing")
                .build();

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCompany)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Beta LLC"));
    }

    @Test
    void testUpdateCompany() throws Exception {
        company.setName("Updated Name");

        mockMvc.perform(put("/api/companies/" + company.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testDeleteCompany() throws Exception {
        mockMvc.perform(delete("/api/companies/" + company.getId()))
                .andExpect(status().isNoContent());

        Optional<Company> deleted = companyRepository.findById(company.getId());
        assert(deleted.isEmpty());
    }
}
