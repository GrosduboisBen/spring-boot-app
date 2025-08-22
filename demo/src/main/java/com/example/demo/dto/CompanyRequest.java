// src/main/java/com/example/demo/dto/CompanyRequest.java
package com.example.demo.dto;

import lombok.Data;

@Data
public class CompanyRequest {
    private String name;
    private String email;
    private String contact;
    private String industry;
}
