package com.ragbot.rag_knowledge_bot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true ,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String role;

    @Column(name= "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
