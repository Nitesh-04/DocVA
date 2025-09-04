package com.example.docva.controller;

import com.example.docva.model.DocumentVersion;
import com.example.docva.service.VersionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/version")
public class VersionController {
    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    private final String username = "user1";

    @GetMapping("/all")
    public List<DocumentVersion> getAllVersions(
            @RequestParam("documentId") Long documentId) {
        return versionService.getAllVersionsByDocumentId(documentId);
    }

    @GetMapping("/latest")
    public Optional<DocumentVersion> getLatestVersion(
            @RequestParam("documentId") Long documentId) {
        return versionService.getLatestVersionByDocumentId(documentId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteVersion(@RequestParam("versionNo") int versionNo, @RequestParam("documentId") Long documentId) {

        Optional<DocumentVersion> version = versionService.deleteVersion(username, versionNo, documentId);

        if(version.isPresent()) {
            return  new ResponseEntity<>("Version deleted", HttpStatus.OK);
        }
        return new  ResponseEntity<>("Version not found", HttpStatus.NOT_FOUND);
    }

}
