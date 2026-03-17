package com.chat.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class ConversationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String user1;

    @Column(nullable = false, length = 50)
    private String user2;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public ConversationEntity() {}

    public ConversationEntity(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public Long getId()             { return id; }
    public String getUser1()        { return user1; }
    public String getUser2()        { return user2; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public String getOtherUser(String me) {
        return me.equals(user1) ? user2 : user1;
    }
}