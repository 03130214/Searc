package com.example.searc.repository;

import com.example.searc.model.InnerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InnerLocationRepository extends JpaRepository<InnerLocation, Long> {
    Optional<InnerLocation> findByName(String name);
}