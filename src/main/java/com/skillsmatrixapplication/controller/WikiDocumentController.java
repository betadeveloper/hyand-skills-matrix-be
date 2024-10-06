package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.persistence.entity.WikiDocument;
import com.skillsmatrixapplication.service.WikiDocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/wiki")
public class WikiDocumentController {

    private final WikiDocumentService wikiDocumentService;

    public WikiDocumentController(WikiDocumentService wikiDocumentService) {
        this.wikiDocumentService = wikiDocumentService;
    }

    @GetMapping
    public List<WikiDocument> getAllDocuments() {
        return wikiDocumentService.getAllDocuments();
    }

    @GetMapping("/category/{category}")
    public List<WikiDocument> getDocumentsByCategory(@PathVariable String category) {
        return wikiDocumentService.getDocumentsByCategory(category);
    }

    @PostMapping("/upload")
    public ResponseEntity<WikiDocument> uploadDocument(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("author") String author,
            @RequestParam("file") MultipartFile file) throws IOException {

        byte[] fileData = file.getBytes();
        String fileType = file.getContentType();
        WikiDocument document = wikiDocumentService.createDocument(title, description, category, author, fileData, fileType);

        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        WikiDocument document = wikiDocumentService.getDocumentById(id);

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        String fileName = document.getTitle();
        String fileType = document.getFileType();

        if (fileType != null) {
            String extension = getFileExtension(fileType);
            if (!fileName.toLowerCase().endsWith(extension)) {
                fileName += extension;
            }
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(document.getFileData());
    }

    private String getFileExtension(String fileType) {
        switch (fileType) {
            case "application/pdf":
                return ".pdf";
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "application/msword":
                return ".doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return ".docx";
            default:
                return "";
        }
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        wikiDocumentService.deleteDocument(id);
    }
}
