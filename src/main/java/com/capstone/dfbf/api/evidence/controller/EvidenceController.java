package com.capstone.dfbf.api.evidence.controller;

import com.capstone.dfbf.api.evidence.dto.ComparisonRes;
import com.capstone.dfbf.api.evidence.dto.VerificationRes;
import com.capstone.dfbf.api.evidence.service.EvidenceService;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "증거물 업로드 기능", description = "검증물과 대조물을 S3 스토리지에 업로드합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
@Slf4j
public class EvidenceController {

    private final EvidenceService evidenceService;

    @Operation(summary = "검증물 업로드", description = "검증물을 S3 스토리지에 업로드합니다.")
    @PostMapping("/upload/verification")
    public VerificationRes uploadVerification(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @RequestPart("verification-file") MultipartFile file) throws IOException {
        return evidenceService.verificationUpload(principalDetails, file);
    }
  
 
    @Operation(summary = "대조물 업로드", description = "대조물을 S3 스토리지에 업로드합니다.")
    @PostMapping("/upload/comparisons")
    public ComparisonRes uploadComparisonFiles(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                               @RequestPart("comparison-file") List<MultipartFile> files) throws IOException {
        return evidenceService.comparisonUpload(principalDetails, files);
    }

    @Operation(summary = "URL 프리사인", description = "S3 스토리지에 저장할 URL을 프리사인합니다.")
    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam("key") String key) {
        String url = evidenceService.getPresignedUrl(key);
        return ResponseEntity.ok(url);
    }
}
