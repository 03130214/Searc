package com.example.searc.model;
import jakarta.persistence.*;

@Entity
@Table(name = "internal_maps")
public class InternalMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mapData;

    // Getters and setters
}
