package com.example.docva.controller;

import com.example.docva.dto.AuditDTO;
import com.example.docva.model.LogType;
import com.example.docva.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
public class AuditController {
    AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    private final String username = "user1";

    @GetMapping("/all")
    public Page<AuditDTO> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return auditService.getUserLogs(page, size,username);
    }

    @GetMapping("/action/{action}")
    public Page<AuditDTO> getLogsByAction(
            @PathVariable LogType action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return auditService.getLogsByAction(page,size,action);
    }

    @GetMapping("/document/{documentId}")
    public Page<AuditDTO> getLogsByAction(
            @PathVariable Long documentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return auditService.getLogsByDocument(page,size,documentId);
    }

    @GetMapping("/user/document/{documentId}")
    public Page<AuditDTO> getLogsUserOnDocument(
            @PathVariable Long documentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return auditService.getLogsByUserOnDocument(page,size,username, documentId);
    }

    @GetMapping("/document/action/{action}")
    public Page<AuditDTO> getLogsByActionOnDocument(
            @PathVariable LogType action,
            @RequestParam("documentId") Long documentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return auditService.getLogsByTypeOnDocument(page,size,action, documentId);
    }


}
