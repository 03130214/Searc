package com.example.searc.service;

import com.example.searc.model.Diary;
import com.example.searc.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryService {
    @Autowired
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }
    public void createDiary(Diary diary){
        Diary newdiary=new Diary();
        newdiary.setUsername(diary.getUsername());
        newdiary.setTitle(diary.getTitle());
        newdiary.setContent(diary.getContent());
        newdiary.setLocation(diary.getLocation());
        diaryRepository.save(newdiary);
    }

    public List<Diary> findDiariesByTitle(String title) {
        return diaryRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Diary> findAllDiaries() {
        return diaryRepository.findAll();
    }

    public List<Diary> findDiariesByLocation(String location) {
        return diaryRepository.findByLocationContainingIgnoreCase(location);
    }
}
