package com.chat.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UserEntity() {}

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId()           { return id; }
    public String getUsername()   { return username; }
    public String getPassword()   { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}