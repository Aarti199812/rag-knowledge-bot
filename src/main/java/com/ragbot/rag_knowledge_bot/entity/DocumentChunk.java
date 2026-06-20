package com.ragbot.rag_knowledge_bot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="document_chunks")
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="document_id")
    private Long documentId;

    @Column(name="chunk_text", columnDefinition = "TEXT")
    private String chunkText;

    @Column(name="chunk_index")
    private Integer chunkIndex;

}
