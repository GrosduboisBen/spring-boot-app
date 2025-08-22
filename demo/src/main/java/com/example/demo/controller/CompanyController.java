package com.example.demo.controller;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;

    // Convert entity → response
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

    // Convert request → entity
    private Company toEntity(CompanyRequest request) {
        return Company.builder()
                .name(request.getName())
                .email(request.getEmail())
                .contact(request.getContact())
                .industry(request.getIndustry())
                .build();
    }

    @GetMapping
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        return companyRepository.findById(id)
                .map(company -> ResponseEntity.ok(toResponse(company)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest request) {
        Company company = toEntity(request);
        Company savedCompany = companyRepository.save(company);
        return ResponseEntity.ok(toResponse(savedCompany));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyRequest request
    ) {
        return companyRepository.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setEmail(request.getEmail());
                    existing.setContact(request.getContact());
                    existing.setIndustry(request.getIndustry());
                    Company updated = companyRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
    return companyRepository.findById(id)
            .map(existing -> {
                companyRepository.delete(existing);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
