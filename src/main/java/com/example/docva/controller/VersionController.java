package com.example.docva.controller;

import com.example.docva.dto.VersionDTO;
import com.example.docva.model.Version;
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
    public List<VersionDTO> getAllVersions(
            @RequestParam("documentId") Long documentId) {
        return versionService.getAllVersionsByDocumentId(documentId);
    }

    @GetMapping("/latest")
    public Optional<VersionDTO> getLatestVersion(
            @RequestParam("documentId") Long documentId) {
        return versionService.getLatestVersionByDocumentId(documentId);
    }

    @DeleteMapping("/{documentId}/{versionNo}")
    public ResponseEntity<String> deleteVersion(@PathVariable int versionNo, @PathVariable Long documentId) {

        Optional<Version> version = versionService.deleteVersion(username, versionNo, documentId);

        if(version.isPresent()) {
            return  new ResponseEntity<>("Version deleted", HttpStatus.OK);
        }
        return new  ResponseEntity<>("Version not found", HttpStatus.NOT_FOUND);
    }

}
