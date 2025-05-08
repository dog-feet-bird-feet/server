package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import com.capstone.dfbf.api.result.dto.ResultUpdateRequest;
import com.capstone.dfbf.api.result.service.HistoryService;
import com.capstone.dfbf.api.result.service.ResultResponse;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "히스토리 기능", description = "히스토리 탭에서 수행되는 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
@RestController
public class HistoryController {

    private final HistoryService historyService;

    @Operation(summary = "히스토리 조회", description = "히스토리(결과 목록)를 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long memberId = principalDetails.getAuthenticatedMember().getMemberId();
        List<HistoryResultResponse> responses = historyService.getHistoryByMemberId(memberId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "결과 조회", description = "히스토리에서 특정 결과를 조회합니다.")
    @GetMapping("/result")
    public ResponseEntity<?> getResult(@RequestParam("id") String id) {
        ResultResponse response = historyService.getResultById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결과 수정", description = "히스토리에서 특정 결과의 제목을 수정합니다.")
    @PatchMapping("/result")
    public ResponseEntity<?> updateResult(@RequestParam("id") String id, @RequestBody @Valid ResultUpdateRequest request) {
        String updatedId = historyService.updateResultName(id, request.newName());
        return ResponseEntity.ok(updatedId + "의 이름이 변경됐습니다.");
    }

    @Operation(summary = "결과 삭제", description = "히스토리에서 특정 결과를 삭제합니다.")
    @DeleteMapping("/result")
    public ResponseEntity<?> deleteHistory(@RequestParam("id") String id) {
        historyService.deleteResult(id);
        return ResponseEntity.ok("삭제에 성공했습니다.");
    }
}
