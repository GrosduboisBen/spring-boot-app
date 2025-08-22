package com.example.demo;

import com.example.demo.model.Company;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompanyControllerTest {

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

    @BeforeEach
    void setUp() {
        // Delete all in order respecting FK constraints
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
    }

    @Test
    void contextLoads() {
    }

    // add other Company CRUD tests here
}
