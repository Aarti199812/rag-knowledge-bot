package com.ragbot.rag_knowledge_bot.repository;

import com.ragbot.rag_knowledge_bot.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatSessionRepository
    extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findByUserId(Long userId);
}