package com.example.docva.dto;

import com.example.docva.model.LogType;

import java.time.LocalDateTime;

public record AuditDTO(Long id, String username, LogType logType, LocalDateTime timestamp, Long documentId) {}
