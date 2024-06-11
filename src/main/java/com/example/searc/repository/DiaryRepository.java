package com.example.searc.repository;

import com.example.searc.model.Diary;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUsername(String username);
    @Modifying
    @Transactional
    @Query("DELETE FROM Diary d WHERE d.id IN :ids")
    void deleteAllById(@Param("ids") List<Long> ids);
    List<Diary> findByTitleContainingIgnoreCase(String title);
    List<Diary> findByLocationContainingIgnoreCase(String location);
    List<Diary> findByUsernameContainingIgnoreCase(String Username);
    @Query("SELECT d FROM Diary d")
    List<Diary> findTopPopularDiaries(Pageable pageable);
}