package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.api.result.service.AppraisalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RequestMapping("/appraisal")
@RestController
public class AppraisalController {

    private AppraisalService appraisalService;

    @PostMapping
    public ResponseEntity<AppraisalResponse> appraisal(@RequestBody AppraisalRequest request) {
        AppraisalResponse response = appraisalService.appraise(request);
        return ResponseEntity.ok(response);
    }
}
