package com.example.demo.controller;

import com.example.demo.dto.MissionRequest;
import com.example.demo.dto.MissionResponse;
import com.example.demo.model.Mission;
import com.example.demo.model.Order;
import com.example.demo.repository.MissionRepository;
import com.example.demo.repository.OrderRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionRepository missionRepository;
    private final OrderRepository orderRepository;

    public MissionController(MissionRepository missionRepository,
                             OrderRepository orderRepository) {
        this.missionRepository = missionRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<MissionResponse> createMission(@RequestBody MissionRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Mission mission = Mission.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .order(order)
                .build();

        Mission saved = missionRepository.save(mission);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public List<MissionResponse> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionResponse> getMissionById(@PathVariable Long id) {
        return missionRepository.findById(id)
                .map(mission -> ResponseEntity.ok(toResponse(mission)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionResponse> updateMission(@PathVariable Long id, @RequestBody MissionRequest request) {
        return missionRepository.findById(id)
                .map(existing -> {
                    Order order = orderRepository.findById(request.getOrderId())
                            .orElseThrow(() -> new RuntimeException("Order not found"));

                    existing.setTitle(request.getTitle());
                    existing.setDescription(request.getDescription());
                    existing.setStartDate(request.getStartDate());
                    existing.setEndDate(request.getEndDate());
                    existing.setOrder(order);

                    Mission updated = missionRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        return missionRepository.findById(id)
                .map(existing -> {
                    missionRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private MissionResponse toResponse(Mission mission) {
        return MissionResponse.builder()
                .id(mission.getId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .startDate(mission.getStartDate())
                .endDate(mission.getEndDate())
                .orderId(mission.getOrder().getId())
                .build();
    }
}
