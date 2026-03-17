package com.chat.app.controller;

import com.chat.app.entity.MessageEntity;
import com.chat.app.model.ChatMessage;
import com.chat.app.repository.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        // Persist every message to PostgreSQL
        MessageEntity entity = new MessageEntity(message.getSender(), message.getContent());
        MessageEntity saved = messageRepository.save(entity);
        message.setId(saved.getId());
        return message;
    }

    // REST endpoint — last 50 messages for chat history on page load
    @GetMapping("/api/messages/recent")
    @ResponseBody
    public List<ChatMessage> recentMessages() {
        return messageRepository
                .findTop50ByOrderBySentAtAsc(PageRequest.of(0, 50))
                .stream()
                .map(e -> {
                    ChatMessage m = new ChatMessage();
                    m.setId(e.getId());
                    m.setSender(e.getSender());
                    m.setContent(e.getContent());
                    return m;
                })
                .toList();
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}