package com.example.demo.controller;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.ProviderRequest;
import com.example.demo.dto.ProviderResponse;
import com.example.demo.model.Provider;
import com.example.demo.repository.ProviderRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderRepository providerRepository;

    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @PostMapping
    public ResponseEntity<?> createProvider(@RequestBody ProviderRequest request) {
        try {
            Provider provider = Provider.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .contact(request.getContact())
                    .category(request.getCategory())
                    .build();

            Provider saved = providerRepository.save(provider);
            return ResponseEntity.ok(toResponse(saved));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while creating the provider."));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProviders() {
        try {
            List<ProviderResponse> providers = providerRepository.findAll().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(providers);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching providers."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderById(@PathVariable Long id) {
        try {
            Optional<Provider> providerOpt = providerRepository.findById(id);
            if (providerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Provider not found with id " + id));
            }

            return ResponseEntity.ok(toResponse(providerOpt.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while fetching the provider."));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchProvider(
            @PathVariable Long id,
            @RequestBody ProviderRequest request) {
        try {
            Optional<Provider> providerOpt = providerRepository.findById(id);
            if (providerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Provider not found with id " + id));
            }

            Provider existing = providerOpt.get();
            if (request.getName() != null) existing.setName(request.getName());
            if (request.getEmail() != null) existing.setEmail(request.getEmail());
            if (request.getContact() != null) existing.setContact(request.getContact());
            if (request.getCategory() != null) existing.setCategory(request.getCategory());

            Provider updated = providerRepository.save(existing);
            return ResponseEntity.ok(toResponse(updated));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while updating the provider."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvider(@PathVariable Long id) {
        try {
            Optional<Provider> providerOpt = providerRepository.findById(id);
            if (providerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Provider not found with id " + id));
            }

            providerRepository.delete(providerOpt.get());
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred while deleting the provider."));
        }
    }

    private ProviderResponse toResponse(Provider provider) {
        return ProviderResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .email(provider.getEmail())
                .contact(provider.getContact())
                .category(provider.getCategory())
                .build();
    }
}
