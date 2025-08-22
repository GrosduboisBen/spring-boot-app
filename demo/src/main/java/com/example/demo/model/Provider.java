package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String contact;

    @Enumerated(EnumType.STRING)
    private Category category;

    public enum Category {
        DEVELOPMENT,
        DESIGN,
        MARKETING,
        CONSULTING
    }
}
