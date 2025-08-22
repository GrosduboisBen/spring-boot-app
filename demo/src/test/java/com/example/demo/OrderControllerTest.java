package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
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
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Company savedCompany;
    private Project savedProject;
    private Provider savedProvider;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        projectRepository.deleteAll();
        providerRepository.deleteAll();
        companyRepository.deleteAll();

        // Create a company
        savedCompany = companyRepository.save(Company.builder()
                .name("OpenAI")
                .email("contact@openai.com")
                .contact("123456789")
                .industry("AI Research")
                .build());

        // Create a project
        savedProject = projectRepository.save(Project.builder()
                .title("AI Chatbot")
                .description("Build an AI Chatbot")
                .budget(50000.0)
                .company(savedCompany)
                .build());

        // Create a provider
        savedProvider = providerRepository.save(Provider.builder()
                .name("Dev Experts")
                .email("contact@devexperts.com")
                .contact("987654321")
                .category(Provider.Category.DEVELOPMENT)
                .build());
    }

    @Test
    void shouldCreateAndFetchOrder() throws Exception {
        String json = """
                {
                  "description": "Develop chatbot module",
                  "quantity": 2,
                  "status": "PENDING",
                  "project": {"id": %d},
                  "provider": {"id": %d}
                }
                """.formatted(savedProject.getId(), savedProvider.getId());

        // Create order
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));

        // Verify repository
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);

        // Fetch via GET
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Develop chatbot module"));
    }
}
