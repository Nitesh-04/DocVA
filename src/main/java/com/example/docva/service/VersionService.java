package com.example.docva.service;

import com.example.docva.dto.VersionDTO;
import com.example.docva.model.Document;
import com.example.docva.model.Version;
import com.example.docva.model.LogType;
import com.example.docva.repository.DocumentRepository;
import com.example.docva.repository.VersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VersionService {
    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;
    private final AuditService auditService;

    public VersionService(DocumentRepository documentRepository, VersionRepository versionRepository, AuditService auditService) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
        this.auditService = auditService;
    }

    public static VersionDTO toDTO(Version version) {
        return new VersionDTO(
                version.getId(),
                version.getVersionNo(),
                version.getVersionLink(),
                version.getUploadedAt(),
                version.getDocument() != null ? version.getDocument().getId() : null
        );
    }

    public static List<VersionDTO> toDTOList(List<Version> versions) {
        return versions.stream().map(VersionService::toDTO).toList();
    }


    public List<VersionDTO> getAllVersionsByDocumentId(Long documentId) {
        return toDTOList(versionRepository.findByDocumentIdOrderByUploadedAtDesc(documentId));
    }

    public Optional<VersionDTO> getLatestVersionByDocumentId(Long documentId) {
        return versionRepository.findTopByDocumentIdOrderByVersionNoDesc(documentId)
                .map(VersionService::toDTO);
    }

    @Transactional
    public Optional<Version> deleteVersion(String username, int versionNo, Long documentId) {
        Version delVersion = versionRepository.findByDocumentIdAndVersionNo(documentId,versionNo)
                .orElse(null);
        Document document = documentRepository.findById(documentId);


        if(delVersion != null) {
            versionRepository.delete(delVersion);

            auditService.createAudit(username,LogType.DELETE_VERSION, delVersion.getDocument());

            if(versionRepository.findTopByDocumentIdOrderByVersionNoDesc(documentId).isPresent()) {
                String newFileLink = versionRepository.findTopByDocumentIdOrderByVersionNoDesc(documentId).get().getVersionLink();
                document.setFileLink(newFileLink);
            }

            else{
                documentRepository.delete(document);
                auditService.createAudit(username,LogType.DELETE_DOCUMENT, delVersion.getDocument());
            }
        }

        return Optional.ofNullable(delVersion);
    }

}
