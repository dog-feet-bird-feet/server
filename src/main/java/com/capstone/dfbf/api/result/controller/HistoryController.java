package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.result.controller.docs.HistoryApi;
import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import com.capstone.dfbf.api.result.dto.ResultUpdateRequest;
import com.capstone.dfbf.api.result.service.HistoryService;
import com.capstone.dfbf.api.result.service.ResultResponse;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
@RestController
public class HistoryController implements HistoryApi {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<HistoryResultResponse>> getHistory(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long memberId = principalDetails.getAuthenticatedMember().getMemberId();
        List<HistoryResultResponse> responses = historyService.getHistoryByMemberId(memberId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/result")
    public ResponseEntity<?> getResult(
            @Parameter(required = true, description = "감정 결과 ID")
            @RequestParam("id") String id
    ) {
        ResultResponse response = historyService.getResultById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/result")
    public ResponseEntity<?> updateResult(
            @Parameter(required = true, description = "감정 결과 ID")
            @RequestParam("id")
            String id,
            @Parameter(required = true, description = "변경할 내용")
            @RequestBody
            @Valid
            ResultUpdateRequest request) {
        String updatedId = historyService.updateResultName(id, request.newName());
        return ResponseEntity.ok(updatedId + "의 이름이 변경됐습니다.");
    }

    @Override
    @DeleteMapping("/result")
    public ResponseEntity<?> deleteHistory(
            @Parameter(required = true, description = "감정 결과 ID")
            @RequestParam("id") String id
    ) {
        historyService.deleteResult(id);
        return ResponseEntity.ok("삭제에 성공했습니다.");
    }
}
