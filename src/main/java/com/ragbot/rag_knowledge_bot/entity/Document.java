package com.ragbot.rag_knowledge_bot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private  String name;

    @Column(name= "file_type")
    private String fileType;

    @Column(name="uploaded_by")
    private  Long uploadedBy;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}
