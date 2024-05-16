package com.example.searc.repository;

import com.example.searc.model.User_Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface UserDiaryRepository extends JpaRepository<User_Diary, Long> {
        Optional<User_Diary> findByDiaryIdAndUsername(Long diaryId, String username);
    }
