package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.model.Order.Status;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderControllerTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private CompanyRepository companyRepository;

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

        company = Company.builder().name("Acme Corp").contact("John").email("john@acme.com").build();
        companyRepository.save(company);

        project = Project.builder().title("Website").description("Redesign").budget(5000.0).company(company).build();
        projectRepository.save(project);

        provider = Provider.builder().name("Dev Solutions").email("dev@solutions.com").contact("Alice").category(Provider.Category.DEVELOPMENT).build();
        providerRepository.save(provider);

        order = Order.builder()
                .description("Develop homepage")
                .quantity(1)
                .status(Status.PENDING)
                .project(project)
                .provider(provider)
                .build();
        orderRepository.save(order);
    }

    @Test
    void contextLoads() {}
}
