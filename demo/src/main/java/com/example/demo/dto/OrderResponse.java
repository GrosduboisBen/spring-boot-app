package com.example.demo.dto;

import com.example.demo.model.Order.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String description;
    private Integer quantity;
    private Status status;
    private Long projectId;
    private Long providerId;
}
