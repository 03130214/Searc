package com.example.searc.repository;

import com.example.searc.model.InnerRoad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InnerRoadRepository extends JpaRepository<InnerRoad, Long> {
    List<InnerRoad> findByStartLocationId(Long startLocationId);
}