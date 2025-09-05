package com.example.docva.dto;

import java.time.LocalDateTime;

public record VersionDTO(Long id, int versionNo, String versionLink, LocalDateTime uploadedAt, Long documentId) {
}
