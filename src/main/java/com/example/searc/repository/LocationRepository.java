package com.example.searc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.searc.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByName(String name);

}
