package com.example.searc.controller;

import com.example.searc.dto.DiaryDTO;
import com.example.searc.model.Diary;
import com.example.searc.model.User_Diary;
import com.example.searc.repository.DiaryRepository;
import com.example.searc.repository.UserDiaryRepository;
import com.example.searc.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DiaryController {
    private final DiaryService diaryService;
    private final DiaryRepository diaryRepository;
    private final UserDiaryRepository UserDiaryRepository;

    public DiaryController(DiaryService diaryService, DiaryRepository diaryRepository, com.example.searc.repository.UserDiaryRepository userDiaryRepository) {
        this.diaryService = diaryService;
        this.diaryRepository = diaryRepository;
        this.UserDiaryRepository = userDiaryRepository;
    }

    @PostMapping("/diaries")
    public ResponseEntity<?> createDiary(@RequestBody DiaryDTO diary) throws IOException {
        diaryService.createDiary(diary);
        return ResponseEntity.ok(diary);
    }

    @GetMapping("/diaries")
    public List<DiaryDTO> getDiaries(@RequestParam(required = false) String title, @RequestParam(required = false) String location, @RequestParam(required = false) String content) {
        if (title != null && !title.isEmpty()) {
            return diaryService.findDiariesByTitle(title);
        }  else if ((location != null && !location.isEmpty())) {
            return diaryService.findDiariesByLocation(location);
        }else if ((content != null && !content.isEmpty())) {
            System.out.println(content);
            return diaryService.findDiariesByContent(content);
        }else {
            return diaryService.findAllDiaries();
        }
    }
//
    @GetMapping("/diaries/{id}")
    public DiaryDTO getDiaryById(@PathVariable Long id) {
        return diaryService.findDiaryById(id);
//        return diary.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/diaries/top")
    public List<DiaryDTO> getTopDiariesByPopularity(@RequestParam int limit) {
        return diaryService.findTopDiariesByPopularity(limit);
    }

    @GetMapping("/diaries/by-user")
    public ResponseEntity<List<DiaryDTO>> getDiariesByUsername(@RequestParam String username) {
        List<DiaryDTO> diaries = diaryService.findDiariesByUsername(username);
        return ResponseEntity.ok(diaries);
    }

    @PostMapping("/diaries/delete")
    public ResponseEntity<?> deleteDiaries(@RequestBody List<Long> ids) {
        try {
            diaryRepository.deleteAllById(ids); // 删除所有指定的日记
            return ResponseEntity.ok().build(); // 返回成功响应
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete diaries");
        }
    }
    @PostMapping("/diaries/{id}/incrementHot")
    public ResponseEntity<Diary> incrementDiaryHot(@PathVariable Long id) {
        return diaryRepository.findById(id)
                .map(diary -> {
                    diary.setHot(diary.getHot() + 1);
                    diaryRepository.save(diary);
                    return ResponseEntity.ok(diary);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/diaries/{diaryId}/check-like")
    public ResponseEntity<Boolean> checkUserDiaryLike(@PathVariable Long diaryId, @RequestParam String username) {
        boolean exists = UserDiaryRepository.findByDiaryIdAndUsername(diaryId, username).isPresent();
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/diaries/{diaryId}/toggle-like")
    public ResponseEntity<?> toggleUserDiaryLike(@PathVariable Long diaryId, @RequestParam String username) {
        Optional<User_Diary> existingEntry = UserDiaryRepository.findByDiaryIdAndUsername(diaryId, username);
        Optional<Diary> diary = diaryRepository.findById(diaryId);
        if (diary.isEmpty()) {
            return ResponseEntity.badRequest().body("Diary not found");
        }
        if (existingEntry.isPresent()) {
            UserDiaryRepository.delete(existingEntry.get());
            diary.get().setLikes(diary.get().getLikes() - 1); // Decrement likes count
            diaryRepository.save(diary.get());
            return ResponseEntity.ok("Unliked successfully");
        } else {
            User_Diary newUserDiary = new User_Diary();
            newUserDiary.setDiaryId(diaryId);
            newUserDiary.setUsername(username);
            UserDiaryRepository.save(newUserDiary);
            diary.get().setLikes(diary.get().getLikes() + 1); // Increment likes count
            diaryRepository.save(diary.get());
            return ResponseEntity.ok("Liked successfully");
        }
    }

}