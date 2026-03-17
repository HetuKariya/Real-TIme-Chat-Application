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

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() { this.sentAt = LocalDateTime.now(); }

    public MessageEntity() {}

    public MessageEntity(String sender, String content, Long conversationId) {
        this.sender = sender;
        this.content = content;
        this.conversationId = conversationId;
    }

    public Long getId()               { return id; }
    public String getSender()         { return sender; }
    public String getContent()        { return content; }
    public Long getConversationId()   { return conversationId; }
    public LocalDateTime getSentAt()  { return sentAt; }
}