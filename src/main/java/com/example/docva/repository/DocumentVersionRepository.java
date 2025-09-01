package com.example.docva.repository;

import com.example.docva.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    List<DocumentVersion> findByDocumentId(Long documentId);

    Optional<DocumentVersion> findTopByDocumentIdOrderByVersionNoDesc(Long documentId); // latest version of a document

    List<DocumentVersion> findByDocumentIdAndUploadedBy(Long documentId, String uploadedBy);

    List<DocumentVersion> findByDocumentIdOrderByUploadedAtDesc(Long documentId);
}
