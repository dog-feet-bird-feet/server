package com.capstone.dfbf.api.result.controller.docs;

import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import com.capstone.dfbf.api.result.dto.ResultUpdateRequest;
import com.capstone.dfbf.global.exception.error.ErrorCode;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "히스토리 기능", description = "히스토리 탭에서 수행되는 API")
public interface HistoryApi {

    @Operation(
            summary = "히스토리 조회",
            description = "히스토리(결과 목록)를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = HistoryResultResponse.class))
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "히스토리를 찾을 수 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": 404, \"code\": \"MEMBER-0000\", \"message\": \"회원이 없습니다.\" }"
                                    )
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<HistoryResultResponse>> getHistory(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @Operation(
            summary = "결과 조회",
            description = "히스토리에서 특정 결과를 조회합니다.",
            responses =  {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HistoryResultResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/result")
    ResponseEntity<?> getResult(
            @Parameter(required = true, description = "감정 결과 ID")
            @RequestParam("id") String id
    );

    @Operation(
            summary = "결과 수정",
            description = "히스토리에서 특정 결과의 제목을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{감정결과ID}의 이름이 변경됐습니다."
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/result")
    ResponseEntity<?> updateResult(
            @Parameter(required = true, description = "감정 결과 ID")
            @RequestParam("id")
            String id,
            @Parameter(required = true, description = "변경할 내용")
            @RequestBody
            @Valid
            ResultUpdateRequest request);

    @Operation(
            summary = "결과 삭제",
            description = "히스토리에서 특정 결과를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "삭제에 성공했습니다."
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/result")
    ResponseEntity<?> deleteHistory(
            @Parameter(required = true, description = "감정 결과 ID")
            @RequestParam("id") String id
    );
}
