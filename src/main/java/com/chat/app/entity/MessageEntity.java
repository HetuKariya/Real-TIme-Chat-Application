package com.chat.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        this.sentAt = LocalDateTime.now();
    }

    public MessageEntity() {}

    public MessageEntity(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public Long getId()             { return id; }
    public String getSender()       { return sender; }
    public String getContent()      { return content; }
    public LocalDateTime getSentAt(){ return sentAt; }
}