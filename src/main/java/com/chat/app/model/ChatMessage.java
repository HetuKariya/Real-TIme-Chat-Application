package com.chat.app.model;

public class ChatMessage {
    private Long id;
    private String sender;
    private String content;
    private Long conversationId;

    public ChatMessage() {}

    public Long getId()                     { return id; }
    public void setId(Long id)              { this.id = id; }
    public String getSender()               { return sender; }
    public void setSender(String sender)    { this.sender = sender; }
    public String getContent()              { return content; }
    public void setContent(String content)  { this.content = content; }
    public Long getConversationId()         { return conversationId; }
    public void setConversationId(Long id)  { this.conversationId = id; }
}