package com.example.searc.dto;
//数据传输对象
public class UserLoginDTO {
    private String username;
    private String password;

    // 构造器、getter 和 setter 方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
