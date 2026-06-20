package com.ragbot.rag_knowledge_bot.service;

import com.ragbot.rag_knowledge_bot.entity.ChatMessage;
import com.ragbot.rag_knowledge_bot.entity.ChatSession;
import com.ragbot.rag_knowledge_bot.entity.DocumentChunk;
import com.ragbot.rag_knowledge_bot.repository.ChatMessageRepository;
import com.ragbot.rag_knowledge_bot.repository.ChatSessionRepository;
import com.ragbot.rag_knowledge_bot.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
   @Autowired
    private final ChatSessionRepository sessionRepository;
   @Autowired
    private final ChatMessageRepository messageRepository;
   @Autowired
    private final DocumentChunkRepository chunkRepository;
   @Autowired
    private final ClaudeApiService claudeApiService;

    public ChatSession createSession(Long userId) {
        ChatSession session = new ChatSession();
        session.setUserId(userId);
        return sessionRepository.save(session);
    }

    // Question puchho
    public String chat(Long sessionId, String question) {

        // Step 1: Saare chunks fetch karo
        List<DocumentChunk> allChunks = chunkRepository.findAll();

        // Step 2: Simple keyword search
        List<String> relevantChunks = allChunks.stream()
            .filter(chunk -> isRelevant(chunk.getChunkText(), question))
            .limit(5)
            .map(DocumentChunk::getChunkText)
            .collect(Collectors.toList());

        // Step 3: Agar koi chunk nahi mila
        if (relevantChunks.isEmpty()) {
            relevantChunks = allChunks.stream()
                .limit(3)
                .map(DocumentChunk::getChunkText)
                .collect(Collectors.toList());
        }

        // Step 4: Claude API se answer lo
        String answer = claudeApiService.askClaude(question, relevantChunks);

        // Step 5: User message save karo
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(sessionId);
        userMessage.setRole("user");
        userMessage.setContent(question);
        messageRepository.save(userMessage);

        // Step 6: Assistant message save karo
        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setSessionId(sessionId);
        assistantMessage.setRole("assistant");
        assistantMessage.setContent(answer);
        messageRepository.save(assistantMessage);

        return answer;
    }

    // Chat history fetch karo
    public List<ChatMessage> getChatHistory(Long sessionId) {
        return messageRepository
            .findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    // Simple keyword matching
    private boolean isRelevant(String chunkText, String question) {
        String[] questionWords = question.toLowerCase().split("\\s+");
        String lowerChunk = chunkText.toLowerCase();
        int matchCount = 0;

        for (String word : questionWords) {
            if (word.length() > 3 && lowerChunk.contains(word)) {
                matchCount++;
            }
        }
        return matchCount > 0;
    }
}