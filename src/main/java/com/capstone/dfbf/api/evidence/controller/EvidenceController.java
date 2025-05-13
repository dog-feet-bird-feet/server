package com.capstone.dfbf.api.evidence.controller;

import com.capstone.dfbf.api.evidence.service.EvidenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class EvidenceController {

    private final EvidenceService evidenceService;

    @PostMapping("/upload/verification")
    public String uploadVerification(@RequestPart("verification-file") MultipartFile file) throws IOException {
        return evidenceService.verificationUpload(file);
    }

    @PostMapping("/upload/comparisons")
    public List<String> uploadComparisonFiles(@RequestPart("comparison-file") List<MultipartFile> files) throws IOException {
        return evidenceService.comparisonUpload(files);
    }
}
