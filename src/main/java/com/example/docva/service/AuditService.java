package com.example.docva.service;

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

@Service
public class AuditService {
    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public Page<Audit> getLogsByAction(int page, int size, LogType logType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByActionOrderByTimestampDesc(logType, pageable);
    }

    public Page<Audit> getUserLogs(int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByUsernameOrderByTimestampDesc(username, pageable);
    }

    public Page<Audit> getLogsByDocument(int page, int size, Long documentId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByDocumentIdOrderByTimestampDesc(documentId, pageable);
    }

    public Page<Audit> getLogsByUserOnDocument(int page, int size, String username, Long documentId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByUsernameAndDocumentIdOrderByTimestampDesc(username, documentId, pageable);
    }

    public Page<Audit> getLogsByTypeOnDocument(int page, int size, LogType logType, Long documentId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditRepository.findByActionAndDocumentIdOrderByTimestampDesc(logType, documentId, pageable);
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
