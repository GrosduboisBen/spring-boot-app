package com.example.demo.controller;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.ProjectRequest;
import com.example.demo.dto.ProjectResponse;
import com.example.demo.model.Company;
import com.example.demo.model.Project;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.ProjectRepository;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createProject(@RequestBody ProjectRequest request) {
        try {
            Optional<Company> companyOpt = companyRepository.findById(request.getCompanyId());
            if (companyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Company not found with id " + request.getCompanyId()));
            }

            Project project = Project.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .budget(request.getBudget())
                    .company(companyOpt.get())
                    .build();

            Project saved = projectRepository.save(project);
            return ResponseEntity.ok(toResponse(saved));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while creating the project."));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects() {
        try {
            List<ProjectResponse> projects = projectRepository.findAll().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching projects."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        try {
            Optional<Project> projectOpt = projectRepository.findById(id);
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Project not found with id " + id));
            }

            return ResponseEntity.ok(toResponse(projectOpt.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching the project."));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        try {
            Optional<Project> projectOpt = projectRepository.findById(id);
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Project not found with id " + id));
            }

            Project existing = projectOpt.get();

            // Mise à jour conditionnelle
            if (request.getTitle() != null) {
                existing.setTitle(request.getTitle());
            }
            if (request.getDescription() != null) {
                existing.setDescription(request.getDescription());
            }
            if (request.getBudget() != null) {
                existing.setBudget(request.getBudget());
            }
            if (request.getCompanyId() != null) {
                Optional<Company> companyOpt = companyRepository.findById(request.getCompanyId());
                if (companyOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ErrorResponse("Company not found with id " + request.getCompanyId()));
                }
                existing.setCompany(companyOpt.get());
            }

            Project updated = projectRepository.save(existing);
            return ResponseEntity.ok(toResponse(updated));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while updating the project."));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            Optional<Project> projectOpt = projectRepository.findById(id);
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Project not found with id " + id));
            }

            projectRepository.delete(projectOpt.get());
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while deleting the project."));
        }
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
