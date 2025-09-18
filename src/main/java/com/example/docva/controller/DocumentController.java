package com.example.docva.controller;

import com.example.docva.dto.DocumentDTO;
import com.example.docva.service.DocumentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("documents")
public class DocumentController {

    private final DocumentService documentService;
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @GetMapping
    public List<DocumentDTO> getUserDocuments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return documentService.getDocumentsByOwner(username);
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestParam("fileName") String fileName,
            @RequestParam("fileLink") String fileLink) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        DocumentDTO document = documentService.uploadDocument(fileName, fileLink, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @GetMapping("/file")
    public List<DocumentDTO> getDocumentsByFileName(
            @RequestParam("filename") String fileName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return documentService.getDocumentsByOwnerAndFileName(username,fileName);
    }

    @GetMapping("/time")
    public List<DocumentDTO> getDocumentsBetween(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime from,
            @RequestParam("to")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime to) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return documentService.getDocumentsCreatedBetween(username, from, to);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDocuments(@RequestParam("id") Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        documentService.deleteDocumentByDocumentId(username, id);

        return new ResponseEntity<>("Document deleted", HttpStatus.OK);
    }

}
