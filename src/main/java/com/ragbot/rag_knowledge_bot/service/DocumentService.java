package com.ragbot.rag_knowledge_bot.service;

import com.ragbot.rag_knowledge_bot.entity.Document;
import com.ragbot.rag_knowledge_bot.entity.DocumentChunk;
import com.ragbot.rag_knowledge_bot.repository.DocumentChunkRepository;
import com.ragbot.rag_knowledge_bot.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;


    public Document uploadDocument(MultipartFile file, Long userId)
        throws IOException {


        String extractedText = extractTextFromPdf(file);


        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setFileType("PDF");
        document.setUploadedBy(userId);
        Document savedDoc = documentRepository.save(document);

        List<String> chunks = chunkText(extractedText, 500);

        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = new DocumentChunk();
            chunk.setDocumentId(savedDoc.getId());
            chunk.setChunkText(chunks.get(i));
            chunk.setChunkIndex(i);
            chunkRepository.save(chunk);
        }

        return savedDoc;
    }

    private String extractTextFromPdf(MultipartFile file)
        throws IOException {
        PDDocument pdDocument = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(pdDocument);
        pdDocument.close();
        return text;
    }


    private List<String> chunkText(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder chunk = new StringBuilder();
        int wordCount = 0;

        for (String word : words) {
            chunk.append(word).append(" ");
            wordCount++;

            if (wordCount >= chunkSize) {
                chunks.add(chunk.toString().trim());
                chunk = new StringBuilder();
                wordCount = 0;
            }
        }

        if (chunk.length() > 0) {
            chunks.add(chunk.toString().trim());
        }

        return chunks;
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public void deleteDocument(Long documentId) {
        chunkRepository.deleteByDocumentId(documentId);
        documentRepository.deleteById(documentId);
    }
}