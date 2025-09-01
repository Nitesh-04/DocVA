package com.example.docva.service;

import com.example.docva.model.DocumentVersion;
import com.example.docva.model.LogType;
import com.example.docva.repository.VersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VersionService {
    private final VersionRepository versionRepository;
    private final AuditService auditService;

    public VersionService(VersionRepository versionRepository, AuditService auditService) {
        this.versionRepository = versionRepository;
        this.auditService = auditService;
    }

    public List<DocumentVersion> getAllVersionsByDocumentId(Long documentId) {
        return versionRepository.findByDocumentIdOrderByUploadedAtDesc(documentId);
    }

    public Optional<DocumentVersion> getLatestVersionByDocumentId(Long documentId) {
        return versionRepository.findTopByDocumentIdOrderByVersionNoDesc(documentId);
    }

    @Transactional
    public Optional<DocumentVersion> deleteVersion(String username, int versionNo, Long documentId) {
        DocumentVersion delVersion = versionRepository.findByDocumentIdAndVersionNo(documentId,versionNo)
                .orElse(null);

        if(delVersion != null) {
            versionRepository.delete(delVersion);
            auditService.createAudit(username,LogType.DELETEVERSION, delVersion.getDocument());
        }

        return Optional.ofNullable(delVersion);
    }

}
