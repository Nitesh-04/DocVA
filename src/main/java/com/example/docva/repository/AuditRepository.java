package com.example.docva.repository;

import com.example.docva.model.Audit;
import com.example.docva.model.LogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    Page<Audit> findByUsernameOrderByTimestampDesc(String username, Pageable pageable);

    Page<Audit> findByDocumentIdOrderByTimestampDesc(Long documentId, Pageable pageable);

    Page<Audit> findByActionOrderByTimestampDesc(LogType logType, Pageable pageable);

    Page<Audit> findByUsernameAndDocumentIdOrderByTimestampDesc(String username, Long documentId, Pageable pageable);

    Page<Audit> findByActionAndDocumentIdOrderByTimestampDesc(LogType logType, Long documentId, Pageable pageable);
}
