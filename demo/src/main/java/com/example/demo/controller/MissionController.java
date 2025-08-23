package com.example.demo.controller;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.MissionRequest;
import com.example.demo.dto.MissionResponse;
import com.example.demo.model.Mission;
import com.example.demo.model.Order;
import com.example.demo.repository.MissionRepository;
import com.example.demo.repository.OrderRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<?> createMission(@RequestBody MissionRequest request) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(request.getOrderId());
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Order not found with id " + request.getOrderId()));
            }

            Mission mission = Mission.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .order(orderOpt.get())
                    .build();

            Mission saved = missionRepository.save(mission);
            return ResponseEntity.ok(toResponse(saved));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while creating the mission."));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMissions() {
        try {
            List<MissionResponse> missions = missionRepository.findAll().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(missions);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching missions."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMissionById(@PathVariable Long id) {
        try {
            Optional<Mission> missionOpt = missionRepository.findById(id);
            if (missionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Mission not found with id " + id));
            }

            return ResponseEntity.ok(toResponse(missionOpt.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching the mission."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMission(@PathVariable Long id, @RequestBody MissionRequest request) {
        try {
            Optional<Mission> missionOpt = missionRepository.findById(id);
            if (missionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Mission not found with id " + id));
            }

            Optional<Order> orderOpt = orderRepository.findById(request.getOrderId());
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Order not found with id " + request.getOrderId()));
            }

            Mission existing = missionOpt.get();
            existing.setTitle(request.getTitle());
            existing.setDescription(request.getDescription());
            existing.setStartDate(request.getStartDate());
            existing.setEndDate(request.getEndDate());
            existing.setOrder(orderOpt.get());

            Mission updated = missionRepository.save(existing);
            return ResponseEntity.ok(toResponse(updated));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while updating the mission."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMission(@PathVariable Long id) {
        try {
            Optional<Mission> missionOpt = missionRepository.findById(id);
            if (missionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Mission not found with id " + id));
            }

            missionRepository.delete(missionOpt.get());
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while deleting the mission."));
        }
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
