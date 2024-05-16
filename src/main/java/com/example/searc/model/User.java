package com.example.searc.model;
import jakarta.persistence.*;
// import java.util.HashSet;
// import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")  // 确保电子邮件唯一
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 25)
    private String username;

    @Column(nullable = false, length = 64) // 密码长度足够存储加密后的密码
    private String userPassword;

    @Column(nullable = false, unique = true, length = 50) // 添加电子邮件字段
    private String email;

    // @OneToMany(mappedBy = "diaryAuthor")
    // private Set<Diary> diaries = new HashSet<>();

    // Getter和Setter方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // 关联其他实体的方法等...
}
