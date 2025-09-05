package com.example.docva.service;

import com.example.docva.dto.AuditDTO;
import com.example.docva.model.Audit;
import com.example.docva.model.Document;
import com.example.docva.model.LogType;
import com.example.docva.repository.AuditRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {
    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public static AuditDTO toDTO(Audit audit) {
        return new AuditDTO(
                audit.getId(),
                audit.getUsername(),
                audit.getAction(),
                audit.getTimestamp(),
                audit.getDocument() != null ? audit.getDocument().getId() : null
        );
    }

    public static List<AuditDTO> toDTOList(List<Audit> audits) {
        return audits.stream().map(AuditService::toDTO).toList();
    }


    public Page<AuditDTO> getLogsByAction(int page, int size, LogType logType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByActionOrderByTimestampDesc(logType, pageable).map(AuditService::toDTO);
    }

    public Page<AuditDTO> getUserLogs(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByUsernameOrderByTimestampDesc(username, pageable).map(AuditService::toDTO);
    }

    public Page<AuditDTO> getLogsByDocument(int page, int size, Long documentId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByDocumentIdOrderByTimestampDesc(documentId, pageable).map(AuditService::toDTO);
    }

    public Page<AuditDTO> getLogsByUserOnDocument(int page, int size, String username, Long documentId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByUsernameAndDocumentIdOrderByTimestampDesc(username, documentId, pageable).map(AuditService::toDTO);
    }

    public Page<AuditDTO> getLogsByTypeOnDocument(int page, int size, LogType logType, Long documentId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByActionAndDocumentIdOrderByTimestampDesc(logType, documentId, pageable).map(AuditService::toDTO);
    }

    @Transactional
    public void createAudit(String username, LogType action, Document document) {
        Audit newaudit = new Audit();
        newaudit.setUsername(username);
        newaudit.setAction(action);
        newaudit.setDocument(document);

        auditRepository.save(newaudit);
    }

}
