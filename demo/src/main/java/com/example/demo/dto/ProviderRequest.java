package com.example.demo.dto;

import com.example.demo.model.Provider.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderRequest {
    private String name;
    private String email;
    private String contact;
    private Category category;
}
