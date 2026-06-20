package com.ragbot.rag_knowledge_bot.repository;

import com.ragbot.rag_knowledge_bot.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Query("Select d FROM Document d WHERE d.uploadedBy= :userId")
    List<Document> findbyUserId(Long userId);
}
