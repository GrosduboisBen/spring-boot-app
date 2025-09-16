package com.example.demo.controller;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.EvaluationRequest;
import com.example.demo.dto.EvaluationResponse;
import com.example.demo.model.Evaluation;
import com.example.demo.model.Mission;
import com.example.demo.repository.EvaluationRepository;
import com.example.demo.repository.MissionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<?> createEvaluation(@RequestBody EvaluationRequest request) {
        try {
            Optional<Mission> missionOpt = missionRepository.findById(request.getMissionId());
            if (missionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Mission not found with id " + request.getMissionId()));
            }

            Evaluation evaluation = Evaluation.builder()
                    .rating(request.getRating())
                    .comment(request.getComment())
                    .mission(missionOpt.get())
                    .build();

            Evaluation saved = evaluationRepository.save(evaluation);
            return ResponseEntity.ok(toResponse(saved));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while creating the evaluation."));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEvaluations() {
        try {
            List<EvaluationResponse> evaluations = evaluationRepository.findAll().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(evaluations);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching evaluations."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvaluationById(@PathVariable Long id) {
        try {
            Optional<Evaluation> evaluationOpt = evaluationRepository.findById(id);
            if (evaluationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Evaluation not found with id " + id));
            }

            return ResponseEntity.ok(toResponse(evaluationOpt.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching the evaluation."));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchEvaluation(@PathVariable Long id,
                                            @RequestBody EvaluationRequest request) {
        try {
            Optional<Evaluation> evaluationOpt = evaluationRepository.findById(id);
            if (evaluationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Evaluation not found with id " + id));
            }

            Evaluation existing = evaluationOpt.get();

            if (request.getRating() != null) existing.setRating(request.getRating());
            if (request.getComment() != null) existing.setComment(request.getComment());

            if (request.getMissionId() != null) {
                Optional<Mission> missionOpt = missionRepository.findById(request.getMissionId());
                if (missionOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ErrorResponse("Mission not found with id " + request.getMissionId()));
                }
                existing.setMission(missionOpt.get());
            }

            Evaluation updated = evaluationRepository.save(existing);
            return ResponseEntity.ok(toResponse(updated));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while updating the evaluation."));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Long id) {
        try {
            Optional<Evaluation> evaluationOpt = evaluationRepository.findById(id);
            if (evaluationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Evaluation not found with id " + id));
            }

            evaluationRepository.delete(evaluationOpt.get());
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while deleting the evaluation."));
        }
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
