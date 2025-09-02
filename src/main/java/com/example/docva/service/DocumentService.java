package com.example.docva.service;

import com.example.docva.model.Document;
import com.example.docva.model.DocumentVersion;
import com.example.docva.model.LogType;
import com.example.docva.repository.DocumentRepository;
import com.example.docva.repository.VersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;
    private final AuditService auditService;

    public DocumentService(DocumentRepository documentRepository, VersionRepository documentVersionRepository, AuditService auditService) {
        this.documentRepository = documentRepository;
        this.versionRepository = documentVersionRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Document uploadDocument(String fileName, String owner) {

        Document document = documentRepository.findByFileName(fileName)
                .orElseGet(() -> {
                    Document newDocument = new Document();
                    newDocument.setFileName(fileName);
                    newDocument.setOwner(owner);
                    return documentRepository.save(newDocument);
                });

        int latestVersion = versionRepository.findTopByDocumentIdOrderByVersionNoDesc(document.getId())
                .map(DocumentVersion::getVersionNo)
                .orElse(0);

        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersionNo(latestVersion+1);
        documentVersion.setVersionLink("filepath/logic");

        document.setFileLink(documentVersion.getVersionLink()); // set latest version link to document

        documentVersion.setDocument(document);

        versionRepository.save(documentVersion);

        auditService.createAudit(owner,LogType.UPLOAD,document);

        return document;
    }

    @Transactional
    public Document getDocumentByFileName(String username, String fileName){
        Document doc = documentRepository.findByFileName(fileName)
                .orElse(null);
        if(doc != null){
            auditService.createAudit(username, LogType.VIEW, doc);
        }

        return doc;
    }

    @Transactional
    public List<Document> getDocumentsByOwner(String owner){
        List<Document> docs = documentRepository.findByOwner(owner);
        for(Document document : docs){
            auditService.createAudit(owner,LogType.VIEW,document);
        }

        return docs;
    }

    @Transactional
    public List<Document> getDocumentsCreatedBetween(String username, LocalDateTime startTime, LocalDateTime endTime){
        List<Document> docs = documentRepository.findByCreatedAtBetween(startTime, endTime);
        for(Document document : docs){
            auditService.createAudit(username,LogType.VIEW,document);
        }
        return docs;
    }

    @Transactional
    public List<Document> getDocumentsByOwnerAndFileName(String owner, String fileName){
        List<Document> docs = documentRepository.searchByOwnerAndFileName(owner, fileName);
        for(Document document : docs){
            auditService.createAudit(owner,LogType.VIEW,document);
        }

        return docs;
    }
}
