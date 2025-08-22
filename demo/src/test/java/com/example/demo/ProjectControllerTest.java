package com.example.demo;

import com.example.demo.model.Company;
import com.example.demo.model.Project;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectControllerTest {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private ProviderRepository providerRepository;

    private Project project;
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
                .name("Acme Corp")
                .contact("John Doe")
                .email("john@acme.com")
                .build();
        companyRepository.save(company);

        project = Project.builder()
                .title("Website Redesign")
                .description("Redesign corporate website")
                .budget(10000.0)
                .company(company)
                .build();
        projectRepository.save(project);
    }

    @Test
    void contextLoads() {}
}
