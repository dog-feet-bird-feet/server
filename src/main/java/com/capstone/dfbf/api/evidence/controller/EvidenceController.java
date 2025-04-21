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
    public void uploadVerification(@RequestPart("verification-file") MultipartFile multipartFile) throws IOException {
        evidenceService.verificationUpload(multipartFile);
    }

    @PostMapping("/upload/comparison")
    public void uploadComparisonFiles(@RequestPart("comparison-file") List<MultipartFile> files) throws IOException {
        evidenceService.comparisonUpload(files);
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam("key") String key) {
        String url = evidenceService.getPresignedUrl(key);
        return ResponseEntity.ok(url);
    }
}
