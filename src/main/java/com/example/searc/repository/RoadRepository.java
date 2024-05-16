package com.example.searc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.searc.model.Road;

public interface RoadRepository extends JpaRepository<Road, Long> {
}