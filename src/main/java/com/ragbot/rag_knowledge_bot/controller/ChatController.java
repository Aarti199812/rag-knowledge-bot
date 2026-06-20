package com.ragbot.rag_knowledge_bot.controller;

import com.ragbot.rag_knowledge_bot.entity.ChatMessage;
import com.ragbot.rag_knowledge_bot.entity.ChatSession;
import com.ragbot.rag_knowledge_bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    // Naya session banao
    @PostMapping("/session")
    public ResponseEntity<ChatSession> createSession(
        @RequestParam Long userId) {
        return ResponseEntity.ok(chatService.createSession(userId));
    }

    // Question puchho
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(
        @RequestParam Long sessionId,
        @RequestParam String question) {
        String answer = chatService.chat(sessionId, question);
        return ResponseEntity.ok(Map.of(
            "question", question,
            "answer", answer
        ));
    }

    // Chat history
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getHistory(
        @PathVariable Long sessionId) {
        return ResponseEntity.ok(
            chatService.getChatHistory(sessionId));
    }
}