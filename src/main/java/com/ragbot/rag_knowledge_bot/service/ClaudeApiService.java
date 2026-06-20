package com.ragbot.rag_knowledge_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class ClaudeApiService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final WebClient  webClient =WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String askClaude(String question, List<String> contextChunks) {

        StringBuilder context = new StringBuilder();
        for (int i = 0; i < contextChunks.size(); i++) {
            context.append("Document ").append(i + 1)
                .append(": ").append(contextChunks.get(i))
                .append("\n\n");
        }

        String prompt = """
                Tum ek company knowledge assistant ho.
                Niche diye gaye documents ke basis pe SIRF jawab do.
                Agar answer documents mein nahi hai toh bolo:
                "Mujhe is baare mein information nahi hai."
                
                --- DOCUMENTS ---
                %s
                -----------------
                
                User ka sawaal: %s
                """.formatted(context.toString(), question);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.1-8b-instant");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "Tum ek helpful company knowledge assistant ho. Sirf diye gaye documents se jawab do.");

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);

        messages.add(systemMsg);
        messages.add(userMsg);

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        try {
            String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            Map<String, Object> responseMap =
                objectMapper.readValue(response, Map.class);
            List<Map<String, Object>> choices =
                (List<Map<String, Object>>) responseMap.get("choices");
            Map<String, Object> msg =
                (Map<String, Object>) choices.get(0).get("message");

            return (String) msg.get("content");

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}