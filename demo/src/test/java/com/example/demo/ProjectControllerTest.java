package com.example.demo;

import com.example.demo.model.Company;
import com.example.demo.model.Project;
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
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyRepository companyRepository;

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
    private Project project;

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
                .title("Website Redesign")
                .description("Redesign corporate website")
                .budget(10000.0)
                .company(company)
                .build();
        project = projectRepository.save(project);
    }

    @Test
    void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testCreateProject() throws Exception {
         String json = """
        {
            "title":"Mobile App",
            "description":"Develop mobile application",
            "budget":15000.0,
            "companyId":%d
        }
        """.formatted(company.getId());

    mockMvc.perform(post("/api/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Mobile App"))
        .andExpect(jsonPath("$.companyId").value(company.getId()));
    }

    @Test
    void testPatchProject() throws Exception {
        String json = """
            {
                "title":"New Title",
                "budget":12000.0
            }
            """;

        mockMvc.perform(patch("/api/projects/" + project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("New Title"))
            .andExpect(jsonPath("$.budget").value(12000.0))
            .andExpect(jsonPath("$.description").value("Redesign corporate website")); // inchangé
    }

    @Test
    void testDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/projects/" + project.getId()))
                .andExpect(status().isNoContent());

        Optional<Project> deleted = projectRepository.findById(project.getId());
        assert(deleted.isEmpty());
    }
}
