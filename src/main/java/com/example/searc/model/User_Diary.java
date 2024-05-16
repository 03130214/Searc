package com.example.searc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_diary")
public class User_Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long diaryId;

    @Column
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(long diaryId) {
        this.diaryId = diaryId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
