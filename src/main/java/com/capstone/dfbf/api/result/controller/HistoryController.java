package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import com.capstone.dfbf.api.result.service.HistoryService;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
@RestController
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long memberId = principalDetails.getAuthenticatedMember().getMemberId();
        List<HistoryResultResponse> responses = historyService.getHistoryByMemberId(memberId);
        return ResponseEntity.ok(responses);
    }
}
