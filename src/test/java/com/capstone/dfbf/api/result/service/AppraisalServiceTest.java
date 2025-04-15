package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.api.result.dto.AppraisalSuccess;
import com.capstone.dfbf.global.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.capstone.dfbf.api.fixture.ResultFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AppraisalServiceTest {

    @InjectMocks
    private AppraisalService appraisalService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ResultRepository resultRepository;

    @Test
    void AI_서버에_분석을_의뢰한다() {
        // given
        AppraisalRequest request = createAppraisalRequest();
        AppraisalResponse expectedResponse = createAppraisalResponse();
        AnalysisResult mockResult = createAnalysisResult();

        ResponseEntity<AppraisalResponse> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(AppraisalResponse.class))
        ).thenReturn(responseEntity);
        when(resultRepository.findById(anyString())).thenReturn(Optional.ofNullable(mockResult));
        when(resultRepository.save(any(AnalysisResult.class))).thenReturn(mockResult);

        // when
        AppraisalSuccess success = appraisalService.appraise(request);

        // then
        assertThat(success.isSuccess()).isEqualTo(true);
        assertThat(success.message()).isEqualTo("감정이 완료되었습니다.");
    }

    @Test
    void AI_서버에_요청_Body_형식이_올바르지_않을_경우_예외가_발생한다() {
        // given
        AppraisalRequest request = createAppraisalRequest();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(AppraisalResponse.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // when
        // then
        assertThatThrownBy(() -> appraisalService.appraise(request))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("400 BAD_REQUEST");
    }

    @Test
    void 감정_ID로_감정_결과를_조회한다() {
        // given
        final String id = "01JRSYFFCD6R6C88JJFA0JTZPB";
        AnalysisResult result = createAnalysisResult();

        when(resultRepository.findById(eq(id)))
                .thenReturn(Optional.ofNullable(result));

        // when
        AppraisalResponse response = appraisalService.getAppraisal(id);

        // then
        assertThat(response.id()).isEqualTo(id);
    }

    @Test
    void 잘못된_감정_ID로_감정_결과_확인시_에러가_발생한다() {
        // given
        final String id = "1";

        when(resultRepository.findById(eq(id)))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> appraisalService.getAppraisal(id))
                .isInstanceOf(BaseException.class)
                .hasMessage("결과를 찾을 수 없습니다.");
    }
}