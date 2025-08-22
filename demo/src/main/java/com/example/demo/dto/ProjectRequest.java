package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {
    private String title;
    private String description;
    private Double budget;
    private Long companyId; // references the company
}
