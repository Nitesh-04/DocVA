package com.example.docva.repository;

import com.example.docva.model.Audit;
import com.example.docva.model.LogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRespository extends JpaRepository<Audit, Long> {

    List<Audit> findByUsernameOrderByTimestampDesc(String username);

    List<Audit> findByDocumentIdOrderByTimestampDesc(Long documentId);

    List<Audit> findByActionOrderByTimestampDesc(LogType logType);

    List<Audit> findByUsernameAndDocumentIdOrderByTimestampDesc(String username, Long documentId);
}
