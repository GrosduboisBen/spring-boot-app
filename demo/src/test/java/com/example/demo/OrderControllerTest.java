package com.example.demo;

import com.example.demo.model.Company;
import com.example.demo.model.Order;
import com.example.demo.model.Project;
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
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;
    private Project project;
    private Provider provider;

    @BeforeEach
    void setUp() {
        evaluationRepository.deleteAll();
        missionRepository.deleteAll();
        orderRepository.deleteAll();
        projectRepository.deleteAll();
        providerRepository.deleteAll();
        companyRepository.deleteAll();

        Company company = Company.builder()
                .name("Test Company")
                .email("test@company.com")
                .contact("123456789")
                .industry("IT")
                .build();
        company = companyRepository.save(company);

        project = Project.builder()
                .title("Test Project")
                .description("Project desc")
                .budget(5000.0)
                .company(company) // ✅ assign a valid company
                .build();
        project = projectRepository.save(project);

        provider = Provider.builder()
                .name("John Doe")
                .email("john@provider.com")
                .contact("123456789")
                .category(Provider.Category.DEVELOPMENT)
                .build();
        provider = providerRepository.save(provider);

        order = Order.builder()
                .description("Test Order")
                .quantity(10)
                .status(Order.Status.PENDING)
                .project(project)
                .provider(provider)
                .build();
        order = orderRepository.save(order);
    }

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testCreateOrder() throws Exception {
         String json = """
        {
            "description":"Build API",
            "quantity":1,
            "status":"PENDING",
            "projectId":%d,
            "providerId":%d
        }
        """.formatted(project.getId(), provider.getId());

    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("Build API"))
        .andExpect(jsonPath("$.projectId").value(project.getId()))
        .andExpect(jsonPath("$.providerId").value(provider.getId()));
    }

    @Test
    void testUpdateOrder() throws Exception {
        String json = """
        {
            "description":"Updated API Order",
            "quantity":3,
            "status":"VALIDATED",
            "projectId":%d,
            "providerId":%d
        }
        """.formatted(project.getId(), provider.getId());

    mockMvc.perform(put("/api/orders/{id}", order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("Updated API Order"))
        .andExpect(jsonPath("$.quantity").value(3))
        .andExpect(jsonPath("$.status").value("VALIDATED"));
    }

    @Test
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/orders/" + order.getId()))
                .andExpect(status().isNoContent());

        Optional<Order> deleted = orderRepository.findById(order.getId());
        assert(deleted.isEmpty());
    }
}
