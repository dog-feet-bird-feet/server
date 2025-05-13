package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalSuccess;
import com.capstone.dfbf.api.result.service.AppraisalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "감정 분석 기능", description = "감정 분석 과정에서 수행되는 API")
@Slf4j
@AllArgsConstructor
@RequestMapping("/appraisal")
@RestController
public class AppraisalController {

    private AppraisalService appraisalService;

    @Operation(summary = "감정하기", description = "감정결과ID와 이미지URL을 AI 서버에 전송하여 감정결과를 받습니다.")
    @PostMapping
    public ResponseEntity<String> appraisal(@RequestBody AppraisalRequest request) {
        AppraisalSuccess success = appraisalService.appraise(request);
        return ResponseEntity.ok(success.message());
    }
}
