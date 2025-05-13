package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalAIResponse;
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
    @Mock
    private MemberRepository memberRepository;

    @Test
    void AI_서버에_분석을_의뢰한다() {
        // given
        AppraisalRequest request = createAppraisalRequest();
        AppraisalAIResponse expectedResponse = createAppraisalResponse();
        AnalysisResult mockResult = createAnalysisResult();
        Member member = Member.builder().id(1L).email("hong123@naver.com").build();

        ResponseEntity<AppraisalAIResponse> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(AppraisalAIResponse.class))
        ).thenReturn(responseEntity);
        when(resultRepository.findById(anyString())).thenReturn(Optional.ofNullable(mockResult));
        when(resultRepository.save(any(AnalysisResult.class))).thenReturn(mockResult);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(member));

        // when
        AppraisalAIResponse response = appraisalService.appraise(1L, request);

        // then
        assertThat(response.inclination()).isNotNull();
    }

    @Test
    void AI_서버에_요청_Body_형식이_올바르지_않을_경우_예외가_발생한다() {
        // given
        AppraisalRequest request = createAppraisalRequest();
        Member member = Member.builder().id(1L).email("hong123@naver.com").build();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(AppraisalAIResponse.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // when
        // then
        assertThatThrownBy(() -> appraisalService.appraise(1L, request))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("400 BAD_REQUEST");
    }
}
