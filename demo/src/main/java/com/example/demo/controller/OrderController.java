package com.example.demo.controller;

import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.model.Order;
import com.example.demo.model.Project;
import com.example.demo.model.Provider;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.ProviderRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProjectRepository projectRepository;
    private final ProviderRepository providerRepository;

    public OrderController(OrderRepository orderRepository,
                           ProjectRepository projectRepository,
                           ProviderRepository providerRepository) {
        this.orderRepository = orderRepository;
        this.projectRepository = projectRepository;
        this.providerRepository = providerRepository;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        Order order = Order.builder()
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .status(request.getStatus())
                .project(project)
                .provider(provider)
                .build();

        Order saved = orderRepository.save(order);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok(toResponse(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        return orderRepository.findById(id)
                .map(existing -> {
                    Project project = projectRepository.findById(request.getProjectId())
                            .orElseThrow(() -> new RuntimeException("Project not found"));
                    Provider provider = providerRepository.findById(request.getProviderId())
                            .orElseThrow(() -> new RuntimeException("Provider not found"));

                    existing.setDescription(request.getDescription());
                    existing.setQuantity(request.getQuantity());
                    existing.setStatus(request.getStatus());
                    existing.setProject(project);
                    existing.setProvider(provider);

                    Order updated = orderRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(existing -> {
                    orderRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .description(order.getDescription())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .projectId(order.getProject().getId())
                .providerId(order.getProvider().getId())
                .build();
    }
}
