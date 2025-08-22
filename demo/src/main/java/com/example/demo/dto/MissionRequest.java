package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long orderId;
}
