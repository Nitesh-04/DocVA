package com.example.docva.repository;

import com.example.docva.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository<DocumentVersion, Long> {

    Optional<DocumentVersion> findTopByDocumentIdOrderByVersionNoDesc(Long documentId); // latest version of a document

    List<DocumentVersion> findByDocumentIdOrderByUploadedAtDesc(Long documentId);

    Optional<DocumentVersion> findByDocumentIdAndVersionNo(Long documentId, int versionNo);
}
