package com.example.demo.controller;

import com.example.demo.dto.ProjectRequest;
import com.example.demo.dto.ProjectResponse;
import com.example.demo.model.Company;
import com.example.demo.model.Project;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.ProjectRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;

    public ProjectController(ProjectRepository projectRepository, CompanyRepository companyRepository) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request) {
        Optional<Company> companyOpt = companyRepository.findById(request.getCompanyId());
        if (companyOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .budget(request.getBudget())
                .company(companyOpt.get())
                .build();

        Project saved = projectRepository.save(project);

        ProjectResponse response = toResponse(saved);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(project -> ResponseEntity.ok(toResponse(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        Optional<Company> companyOpt = companyRepository.findById(request.getCompanyId());
        if (companyOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return projectRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(request.getTitle());
                    existing.setDescription(request.getDescription());
                    existing.setBudget(request.getBudget());
                    existing.setCompany(companyOpt.get());
                    Project updated = projectRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(existing -> {
                    projectRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build(); // ✅ fixed type issue
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .budget(project.getBudget())
                .companyId(project.getCompany().getId())
                .companyName(project.getCompany().getName())
                .build();
    }
}
