package com.example.demo.controller;

import com.example.demo.model.Provider;
import com.example.demo.repository.ProviderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderRepository providerRepository;

    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @GetMapping
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id) {
        return providerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Provider createProvider(@RequestBody Provider provider) {
        return providerRepository.save(provider);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long id, @RequestBody Provider providerDetails) {
        return providerRepository.findById(id).map(provider -> {
            provider.setName(providerDetails.getName());
            provider.setEmail(providerDetails.getEmail());
            provider.setContact(providerDetails.getContact());
            provider.setCategory(providerDetails.getCategory());
            return ResponseEntity.ok(providerRepository.save(provider));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        return providerRepository.findById(id).map(provider -> {
            providerRepository.delete(provider);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
