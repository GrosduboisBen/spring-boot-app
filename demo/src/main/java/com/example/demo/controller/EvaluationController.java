package com.example.demo.controller;

import com.example.demo.dto.EvaluationRequest;
import com.example.demo.dto.EvaluationResponse;
import com.example.demo.model.Evaluation;
import com.example.demo.model.Mission;
import com.example.demo.repository.EvaluationRepository;
import com.example.demo.repository.MissionRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationRepository evaluationRepository;
    private final MissionRepository missionRepository;

    public EvaluationController(EvaluationRepository evaluationRepository,
                                MissionRepository missionRepository) {
        this.evaluationRepository = evaluationRepository;
        this.missionRepository = missionRepository;
    }

    @PostMapping
    public ResponseEntity<EvaluationResponse> createEvaluation(@RequestBody EvaluationRequest request) {
        Mission mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        Evaluation evaluation = Evaluation.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .mission(mission)
                .build();

        Evaluation saved = evaluationRepository.save(evaluation);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public List<EvaluationResponse> getAllEvaluations() {
        return evaluationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponse> getEvaluationById(@PathVariable Long id) {
        return evaluationRepository.findById(id)
                .map(evaluation -> ResponseEntity.ok(toResponse(evaluation)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationResponse> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationRequest request) {
        return evaluationRepository.findById(id)
                .map(existing -> {
                    Mission mission = missionRepository.findById(request.getMissionId())
                            .orElseThrow(() -> new RuntimeException("Mission not found"));

                    existing.setRating(request.getRating());
                    existing.setComment(request.getComment());
                    existing.setMission(mission);

                    Evaluation updated = evaluationRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        return evaluationRepository.findById(id)
                .map(existing -> {
                    evaluationRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private EvaluationResponse toResponse(Evaluation evaluation) {
        return EvaluationResponse.builder()
                .id(evaluation.getId())
                .rating(evaluation.getRating())
                .comment(evaluation.getComment())
                .missionId(evaluation.getMission().getId())
                .build();
    }
}
