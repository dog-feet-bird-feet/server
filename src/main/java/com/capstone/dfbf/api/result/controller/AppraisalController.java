package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.api.result.dto.AppraisalSuccess;
import com.capstone.dfbf.api.result.service.AppraisalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/appraisal")
@RestController
public class AppraisalController {

    private AppraisalService appraisalService;

    @PostMapping
    public ResponseEntity<String> appraisal(@RequestBody AppraisalRequest request) {
        AppraisalSuccess success = appraisalService.appraise(request);
        return ResponseEntity.ok(success.message());
    }

    @GetMapping
    public ResponseEntity<AppraisalResponse> getAppraisal(@RequestParam String id) {
        AppraisalResponse response = appraisalService.getAppraisal(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAppraisal(@RequestParam String id) {
        appraisalService.deleteAppraisal(id);
        return ResponseEntity.ok("감정이 삭제되었습니다.");
    }
}
