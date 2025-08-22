package com.example.demo.controller;

import com.example.demo.model.Evaluation;
import com.example.demo.repository.EvaluationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationRepository evaluationRepository;

    public EvaluationController(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @GetMapping
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        return evaluationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Evaluation createEvaluation(@RequestBody Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id, @RequestBody Evaluation evaluationDetails) {
        return evaluationRepository.findById(id).map(evaluation -> {
            evaluation.setRating(evaluationDetails.getRating());
            evaluation.setComment(evaluationDetails.getComment());
            evaluation.setMission(evaluationDetails.getMission());
            return ResponseEntity.ok(evaluationRepository.save(evaluation));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        return evaluationRepository.findById(id).map(evaluation -> {
            evaluationRepository.delete(evaluation);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
