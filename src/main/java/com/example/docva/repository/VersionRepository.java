package com.example.docva.repository;

import com.example.docva.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

    Optional<Version> findTopByDocumentIdOrderByVersionNoDesc(Long documentId); // latest version of a document

    List<Version> findByDocumentIdOrderByUploadedAtDesc(Long documentId);

    Optional<Version> findByDocumentIdAndVersionNo(Long documentId, int versionNo);
}
