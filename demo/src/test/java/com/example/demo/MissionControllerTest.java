package com.example.demo;

import com.example.demo.dto.MissionRequest;
import com.example.demo.model.Company;
import com.example.demo.model.Mission;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
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

    private Mission mission;
    private Order order;
    private Project project;
    private Provider provider;
    private Company company;

    @BeforeEach
    void setUp() {
        evaluationRepository.deleteAll();
        missionRepository.deleteAll();
        orderRepository.deleteAll();
        projectRepository.deleteAll();
        providerRepository.deleteAll();
        companyRepository.deleteAll();

        company = Company.builder()
                .name("Acme Inc.")
                .email("contact@acme.com")
                .contact("John Doe")
                .industry("IT")
                .build();
        company = companyRepository.save(company);

        project = Project.builder()
                .title("Test Project")
                .description("Project desc")
                .budget(5000.0)
                .company(company) // optional dummy company
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

        mission = Mission.builder()
                .title("Test Mission")
                .description("Mission desc")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .order(order)
                .build();
        mission = missionRepository.save(mission);
    }

    @Test
    void testGetAllMissions() throws Exception {
        mockMvc.perform(get("/api/missions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testCreateMission() throws Exception {
        String json = """
        {
            "title":"New Mission",
            "description":"Test mission",
            "startDate":"%s",
            "endDate":"%s",
            "orderId":%d
        }
        """.formatted(LocalDate.now(), LocalDate.now().plusDays(5), order.getId());

    mockMvc.perform(post("/api/missions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("New Mission"))
        .andExpect(jsonPath("$.orderId").value(order.getId()));
    }

    @Test
    void testPatchMission() throws Exception {
        // Crée un DTO partiel pour mettre à jour seulement le titre et la date de fin
        MissionRequest request = MissionRequest.builder()
                .title("Updated Mission")
                .endDate(LocalDate.now().plusDays(10))
                .build();

        mockMvc.perform(patch("/api/missions/{id}", mission.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Mission"))
                .andExpect(jsonPath("$.description").value(mission.getDescription())) // inchangé
                .andExpect(jsonPath("$.endDate").value(LocalDate.now().plusDays(10).toString()));
    }


    @Test
    void testDeleteMission() throws Exception {
        mockMvc.perform(delete("/api/missions/" + mission.getId()))
                .andExpect(status().isNoContent());

        Optional<Mission> deleted = missionRepository.findById(mission.getId());
        assert(deleted.isEmpty());
    }
}
