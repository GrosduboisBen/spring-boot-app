package com.example.demo.controller;

import com.example.demo.dto.ProviderRequest;
import com.example.demo.dto.ProviderResponse;
import com.example.demo.model.Provider;
import com.example.demo.repository.ProviderRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderRepository providerRepository;

    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @PostMapping
    public ResponseEntity<ProviderResponse> createProvider(@RequestBody ProviderRequest request) {
        Provider provider = Provider.builder()
                .name(request.getName())
                .email(request.getEmail())
                .contact(request.getContact())
                .category(request.getCategory())
                .build();

        Provider saved = providerRepository.save(provider);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public List<ProviderResponse> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> getProviderById(@PathVariable Long id) {
        return providerRepository.findById(id)
                .map(provider -> ResponseEntity.ok(toResponse(provider)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> updateProvider(@PathVariable Long id, @RequestBody ProviderRequest request) {
        return providerRepository.findById(id)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setEmail(request.getEmail());
                    existing.setContact(request.getContact());
                    existing.setCategory(request.getCategory());
                    Provider updated = providerRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        return providerRepository.findById(id)
                .map(existing -> {
                    providerRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build(); // ✅ fixed type issue
                })
                .orElse(ResponseEntity.notFound().build());
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
