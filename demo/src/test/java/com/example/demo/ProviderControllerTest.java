package com.example.demo;

import com.example.demo.model.Provider;
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
class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CompanyRepository companyRepository;
    
    private Provider provider;

    @BeforeEach
    void setUp() {
        evaluationRepository.deleteAll();
        missionRepository.deleteAll();
        orderRepository.deleteAll();
        projectRepository.deleteAll();
        providerRepository.deleteAll();
        companyRepository.deleteAll();

        provider = Provider.builder()
                .name("John Doe")
                .email("john@provider.com")
                .contact("123456789")
                .category(Provider.Category.DEVELOPMENT)
                .build();
        provider = providerRepository.save(provider);
    }

    @Test
    void testGetAllProviders() throws Exception {
        mockMvc.perform(get("/api/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testCreateProvider() throws Exception {
        Provider newProvider = Provider.builder()
                .name("Jane Smith")
                .email("jane@provider.com")
                .contact("987654321")
                .category(Provider.Category.DESIGN)
                .build();

        mockMvc.perform(post("/api/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProvider)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.category").value("DESIGN"));
    }

    @Test
    void testUpdateProvider() throws Exception {
        provider.setName("Updated Name");

        mockMvc.perform(put("/api/providers/" + provider.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(provider)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testDeleteProvider() throws Exception {
        mockMvc.perform(delete("/api/providers/" + provider.getId()))
                .andExpect(status().isNoContent());

        Optional<Provider> deleted = providerRepository.findById(provider.getId());
        assert(deleted.isEmpty());
    }
}
