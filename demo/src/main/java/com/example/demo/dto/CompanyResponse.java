// src/main/java/com/example/demo/dto/CompanyResponse.java
package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {
    private Long id;
    private String name;
    private String email;
    private String contact;
    private String industry;
    private LocalDateTime createdAt;
}
