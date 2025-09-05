package com.example.docva.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DocumentDTO (Long id, String fileName, String owner, String fileLink, LocalDateTime createdAt, List<VersionDTO> versions){}