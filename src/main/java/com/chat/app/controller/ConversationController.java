package com.chat.app.controller;

import com.chat.app.entity.ConversationEntity;
import com.chat.app.entity.MessageEntity;
import com.chat.app.entity.UserEntity;
import com.chat.app.model.ChatMessage;
import com.chat.app.repository.ConversationRepository;
import com.chat.app.repository.MessageRepository;
import com.chat.app.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ConversationController(ConversationRepository conversationRepository,
                                  MessageRepository messageRepository,
                                  UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/conversations/open")
    public ResponseEntity<Map<String, Object>> openConversation(
            @AuthenticationPrincipal UserDetails me,
            @RequestBody Map<String, String> body) {

        String otherUsername = body.get("username");
        String myUsername = me.getUsername();

        if (!userRepository.existsByUsername(otherUsername)) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        ConversationEntity conv = conversationRepository
                .findBetween(myUsername, otherUsername)
                .orElseGet(() -> conversationRepository.save(
                        new ConversationEntity(myUsername, otherUsername)));

        return ResponseEntity.ok(Map.of(
                "conversationId", conv.getId(),
                "otherUser", otherUsername
        ));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> listConversations(
            @AuthenticationPrincipal UserDetails me) {

        String myUsername = me.getUsername();
        List<ConversationEntity> convs = conversationRepository.findAllForUser(myUsername);

        List<Map<String, Object>> result = convs.stream().map(c -> {
            List<MessageEntity> msgs = messageRepository
                    .findTop50ByConversationIdOrderBySentAtAsc(c.getId());
            MessageEntity last = msgs.isEmpty() ? null : msgs.get(msgs.size() - 1);

            return Map.<String, Object>of(
                    "conversationId", c.getId(),
                    "otherUser", c.getOtherUser(myUsername),
                    "lastMessage", last != null ? last.getContent() : "",
                    "lastSender", last != null ? last.getSender() : ""
            );
        }).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails me) {

        List<ChatMessage> messages = messageRepository
                .findTop50ByConversationIdOrderBySentAtAsc(id)
                .stream()
                .map(e -> {
                    ChatMessage m = new ChatMessage();
                    m.setId(e.getId());
                    m.setSender(e.getSender());
                    m.setContent(e.getContent());
                    m.setConversationId(e.getConversationId());
                    return m;
                }).toList();

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<String>> searchUsers(
            @RequestParam String q,
            @AuthenticationPrincipal UserDetails me) {

        List<String> users = userRepository.findByUsernameContainingIgnoreCase(q)
                .stream()
                .map(UserEntity::getUsername)
                .filter(u -> !u.equals(me.getUsername()))
                .limit(10)
                .toList();

        return ResponseEntity.ok(users);
    }
}