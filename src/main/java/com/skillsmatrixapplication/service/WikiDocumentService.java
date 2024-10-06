package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.WikiDocument;
import com.skillsmatrixapplication.persistence.repository.WikiDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WikiDocumentService {

    private final WikiDocumentRepository wikiDocumentRepository;

    public WikiDocumentService(WikiDocumentRepository wikiDocumentRepository) {
        this.wikiDocumentRepository = wikiDocumentRepository;
    }

    public List<WikiDocument> getAllDocuments() {
        return wikiDocumentRepository.findAll();
    }


    public List<WikiDocument> getDocumentsByCategory(String category) {
        return wikiDocumentRepository.findByCategory(category);
    }

    public WikiDocument createDocument(String title, String description, String category, String author, byte[] fileData, String fileType) {
        WikiDocument document = new WikiDocument();
        document.setTitle(title);
        document.setDescription(description);
        document.setCategory(category);
        document.setAuthor(author);
        document.setFileData(fileData);
        document.setFileType(fileType);
        System.out.println("File size: " + fileData.length);
        return wikiDocumentRepository.save(document);
    }

    public WikiDocument getDocumentById(Long id) {
        return wikiDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public void deleteDocument(Long id) {
        wikiDocumentRepository.deleteById(id);
    }
}
