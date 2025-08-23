package com.example.demo.controller;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderResponse;
import com.example.demo.model.Order;
import com.example.demo.model.Project;
import com.example.demo.model.Provider;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.ProviderRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        try {
            Optional<Project> projectOpt = projectRepository.findById(request.getProjectId());
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Project not found with id " + request.getProjectId()));
            }

            Optional<Provider> providerOpt = providerRepository.findById(request.getProviderId());
            if (providerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Provider not found with id " + request.getProviderId()));
            }

            Order order = Order.builder()
                    .description(request.getDescription())
                    .quantity(request.getQuantity())
                    .status(request.getStatus())
                    .project(projectOpt.get())
                    .provider(providerOpt.get())
                    .build();

            Order saved = orderRepository.save(order);
            return ResponseEntity.ok(toResponse(saved));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while creating the order."));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderResponse> orders = orderRepository.findAll().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching orders."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Order not found with id " + id));
            }

            return ResponseEntity.ok(toResponse(orderOpt.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching the order."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Order not found with id " + id));
            }

            Optional<Project> projectOpt = projectRepository.findById(request.getProjectId());
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Project not found with id " + request.getProjectId()));
            }

            Optional<Provider> providerOpt = providerRepository.findById(request.getProviderId());
            if (providerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Provider not found with id " + request.getProviderId()));
            }

            Order existing = orderOpt.get();
            existing.setDescription(request.getDescription());
            existing.setQuantity(request.getQuantity());
            existing.setStatus(request.getStatus());
            existing.setProject(projectOpt.get());
            existing.setProvider(providerOpt.get());

            Order updated = orderRepository.save(existing);
            return ResponseEntity.ok(toResponse(updated));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while updating the order."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Order not found with id " + id));
            }

            orderRepository.delete(orderOpt.get());
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while deleting the order."));
        }
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
