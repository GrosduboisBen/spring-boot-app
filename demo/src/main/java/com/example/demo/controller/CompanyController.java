package com.example.demo.controller;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.ErrorResponse;
import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;

    private CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .email(company.getEmail())
                .contact(company.getContact())
                .industry(company.getIndustry())
                .createdAt(company.getCreatedAt())
                .build();
    }

    private Company toEntity(CompanyRequest request) {
        return Company.builder()
                .name(request.getName())
                .email(request.getEmail())
                .contact(request.getContact())
                .industry(request.getIndustry())
                .build();
    }

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        try {
            List<CompanyResponse> companies = companyRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching companies."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            var companyOpt = companyRepository.findById(id);
            if (companyOpt.isPresent()) {
                return ResponseEntity.ok(toResponse(companyOpt.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Company not found with id " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching the company."));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody CompanyRequest request) {
        try {
            Company savedCompany = companyRepository.save(toEntity(request));
            return ResponseEntity.ok(toResponse(savedCompany));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while creating the company."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody CompanyRequest request) {
        try {
            var companyOpt = companyRepository.findById(id);
            if (companyOpt.isPresent()) {
                Company existing = companyOpt.get();
                existing.setName(request.getName());
                existing.setEmail(request.getEmail());
                existing.setContact(request.getContact());
                existing.setIndustry(request.getIndustry());
                Company updated = companyRepository.save(existing);
                return ResponseEntity.ok(toResponse(updated));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Company not found with id " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while updating the company."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            var companyOpt = companyRepository.findById(id);
            if (companyOpt.isPresent()) {
                companyRepository.delete(companyOpt.get());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Company not found with id " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while deleting the company."));
        }
    }
}
