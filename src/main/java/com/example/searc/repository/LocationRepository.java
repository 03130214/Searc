package com.example.searc.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.searc.model.Dishes;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.searc.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByName(String name);
    List<Location> findByNameContainingIgnoreCase(String name);

    List<Location> findByTypeContainingIgnoreCase(String type);
}
