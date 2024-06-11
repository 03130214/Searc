package com.example.searc.repository;

import com.example.searc.model.Dishes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishesRepository extends JpaRepository<Dishes, Long> {
    List<Dishes> findByNameContainingIgnoreCase(String name);

    List<Dishes> findByKindContainingIgnoreCase(String kind);

    List<Dishes> findByLocationContainingIgnoreCase(String location);
}
