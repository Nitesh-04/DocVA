package com.example.docva.service;

import com.example.docva.dto.DocumentDTO;
import com.example.docva.dto.VersionDTO;
import com.example.docva.model.Document;
import com.example.docva.model.Version;
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

    public static DocumentDTO toDTO(Document document) {
        List<VersionDTO> versionDTOs = document.getVersions().stream()
                .map(v -> new VersionDTO(
                        v.getId(),
                        v.getVersionNo(),
                        v.getVersionLink(),
                        v.getUploadedAt(),
                        document.getId()
                ))
                .toList();

        return new DocumentDTO(
                document.getId(),
                document.getFileName(),
                document.getOwner(),
                document.getFileLink(),
                document.getCreatedAt(),
                versionDTOs
        );
    }


    public static List<DocumentDTO> toDtoList(List<Document> docs) {
        return docs.stream().map(DocumentService::toDTO).toList();
    }

    @Transactional
    public DocumentDTO uploadDocument(String fileName, String fileLink, String owner) {

        Document document = documentRepository.findByFileName(fileName)
                .orElseGet(() -> {
                    Document newDocument = new Document();
                    newDocument.setFileName(fileName);
                    newDocument.setFileLink(fileLink);
                    newDocument.setOwner(owner);
                    newDocument.setCreatedAt(LocalDateTime.now());
                    return documentRepository.save(newDocument);
                });

        int latestVersion = versionRepository.findTopByDocumentIdOrderByVersionNoDesc(document.getId())
                .map(Version::getVersionNo)
                .orElse(0);

        Version version = new Version();
        version.setVersionNo(latestVersion+1);
        version.setVersionLink(fileLink);
        version.setUploadedAt(LocalDateTime.now());

        document.setFileLink(version.getVersionLink()); // set latest version link to document

        version.setDocument(document);

        versionRepository.save(version);

        auditService.createAudit(owner,LogType.UPLOAD,document);

        return toDTO(document);
    }

    @Transactional
    public List<DocumentDTO> getDocumentsByOwner(String owner){
        List<Document> docs = documentRepository.findByOwner(owner);
        for(Document document : docs){
            auditService.createAudit(owner,LogType.VIEW,document);
        }

        return toDtoList(docs);
    }

    @Transactional
    public List<DocumentDTO> getDocumentsCreatedBetween(String username, LocalDateTime startTime, LocalDateTime endTime){
        List<Document> docs = documentRepository.findByCreatedAtBetween(startTime, endTime);
        for(Document document : docs){
            auditService.createAudit(username,LogType.VIEW,document);
        }
        return toDtoList(docs);
    }

    @Transactional
    public List<DocumentDTO> getDocumentsByOwnerAndFileName(String owner, String fileName){
        List<Document> docs = documentRepository.searchByOwnerAndFileName(owner, fileName);
        for(Document document : docs){
            auditService.createAudit(owner,LogType.VIEW,document);
        }

        return toDtoList(docs);
    }

    @Transactional
    public Document deleteDocumentByDocumentId(String owner, Long documentId) {
        Document deleteDoc =  documentRepository.findById(documentId);
        documentRepository.delete(deleteDoc);

        auditService.createAudit(owner, LogType.DELETE_DOCUMENT, deleteDoc);

        return deleteDoc;
    }
}

