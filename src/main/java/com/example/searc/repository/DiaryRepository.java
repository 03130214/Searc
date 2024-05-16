package com.example.searc.repository;

import com.example.searc.model.Diary;

import java.util.List;
import java.util.Optional;

import com.example.searc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByUsername(String username);

    Optional<Diary> findBytitle(String title);
    List<Diary> findByTitleContainingIgnoreCase(String title);
    List<Diary> findByLocationContainingIgnoreCase(String location);
}