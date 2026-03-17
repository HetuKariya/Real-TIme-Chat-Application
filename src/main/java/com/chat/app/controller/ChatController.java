package com.chat.app.controller;

import com.chat.app.entity.MessageEntity;
import com.chat.app.model.ChatMessage;
import com.chat.app.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageRepository messageRepository,
                          SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/dm")
    public void sendDm(@Payload ChatMessage message) {
        // Persist to PostgreSQL
        MessageEntity entity = new MessageEntity(
                message.getSender(),
                message.getContent(),
                message.getConversationId()
        );
        MessageEntity saved = messageRepository.save(entity);
        message.setId(saved.getId());

        // Send to private conversation topic
        messagingTemplate.convertAndSend(
                "/topic/dm/" + message.getConversationId(), message);
    }

    @GetMapping("/chat")
    public String chat() { return "chat"; }
}