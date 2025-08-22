package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {
    private String name;
    private String email;
    private String contact;
    private String industry;
}
