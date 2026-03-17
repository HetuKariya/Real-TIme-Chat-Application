package com.chat.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_sender_recipient", columnList = "sender, recipient"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;

    // recipient = the other user in the DM conversation
    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) this.timestamp = LocalDateTime.now();
    }

    public ChatMessage() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}