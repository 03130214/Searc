package com.example.searc.service;

import com.example.searc.dto.DiaryDTO;
import com.example.searc.model.Diary;
import com.example.searc.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiaryService {
    @Autowired
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(DiaryDTO diary) throws IOException {
        byte[] compressedContent = Compression.compress(diary.getContent());
        Diary newdiary = new Diary();
        newdiary.setUsername(diary.getUsername());
        newdiary.setTitle(diary.getTitle());
        newdiary.setContent(compressedContent);
        newdiary.setLocation(diary.getLocation());
        diaryRepository.save(newdiary);
    }

    public List<DiaryDTO> findDiariesByTitle(String title) {
        return diaryRepository.findByTitleContainingIgnoreCase(title).stream().map(this::convertToDto).collect(Collectors.toList());
    }
    public List<DiaryDTO> findDiariesByUsername(String Username) {
        return diaryRepository.findByUsernameContainingIgnoreCase(Username).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<DiaryDTO> findAllDiaries() {
        return diaryRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }
    public List<DiaryDTO> findTopDiariesByPopularity(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("hot").descending());
        return diaryRepository.findTopPopularDiaries(pageable).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<DiaryDTO> findDiariesByLocation(String location) {
        return diaryRepository.findByLocationContainingIgnoreCase(location).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public DiaryDTO findDiaryById(Long id) {
        Optional<Diary> optionalDiary = diaryRepository.findById(id);
        return optionalDiary.map(this::convertToDto).orElse(null);
    }
    private DiaryDTO convertToDto(Diary diary) {
        DiaryDTO diaryDto = new DiaryDTO();
        diaryDto.setId(diary.getId());
        diaryDto.setTitle(diary.getTitle());
        diaryDto.setLocation(diary.getLocation());
        diaryDto.setUsername(diary.getUsername());
        diaryDto.setHot(diary.getHot());
        diaryDto.setLikes(diary.getLikes());
        try {
            diaryDto.setContent(Compression.decompress(diary.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
            diaryDto.setContent("");
        }
        return diaryDto;
    }

    public List<DiaryDTO> findDiariesByContent(String content) {
        return diaryRepository.findAll().stream()
                .filter(diary -> {
                    try {
                        String decompressedContent = Compression.decompress(diary.getContent());
                        return decompressedContent.contains(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}

