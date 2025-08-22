package com.example.demo;

import com.example.demo.model.Provider;
import com.example.demo.model.Provider.Category;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProviderControllerTest {

    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;

    private Provider provider;

    @BeforeEach
    void setUp() {
        evaluationRepository.deleteAll();
        missionRepository.deleteAll();
        orderRepository.deleteAll();
        providerRepository.deleteAll();

        provider = Provider.builder()
                .name("Dev Solutions")
                .email("dev@solutions.com")
                .contact("Alice")
                .category(Category.DEVELOPMENT)
                .build();
        providerRepository.save(provider);
    }

    @Test
    void contextLoads() {}
}
