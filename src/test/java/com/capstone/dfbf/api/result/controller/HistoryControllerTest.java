package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.annotation.WithCustomMockUser;
import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.injector.MockitoBeanInjector;
import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.ResultUpdateRequest;
import com.capstone.dfbf.api.result.service.HistoryService;
import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class HistoryControllerTest extends MockitoBeanInjector {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Mock
    private PrincipalDetails principalDetails;

    @BeforeEach
    void setUp() {
        Member member = Member.builder().id(1L).email("hong123@gmail.com").role(Member.Role.USER).nickname("길동").build();
        when(principalDetails.getAuthenticatedMember()).thenReturn(AuthenticatedMember.from(member));
    }

    @Test
    @WithCustomMockUser
    void 회원의_히스토리를_정상적으로_조회한다() throws Exception {
        // given
        AnalysisResult result1 = ResultFixture.createAnalysisResult();
        AnalysisResult result2 = ResultFixture.createAnalysisResult();
        when(resultRepository.findByMember(any(Long.class))).thenReturn(List.of(result1, result2));
        when(memberRepository.existsById(anyLong())).thenReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/history")
                        .with(user(principalDetails))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(result1.getId()))
                .andExpect(jsonPath("$[0].name").value(result1.getTitle()))
                .andExpect(jsonPath("$[0].verificationImgUrl").value(result1.getVerificationImgUrl()))

                .andExpect(jsonPath("$[1].id").value(result2.getId()))
                .andExpect(jsonPath("$[1].name").value(result2.getTitle()))
                .andExpect(jsonPath("$[1].verificationImgUrl").value(result2.getVerificationImgUrl()));
    }

    @Test
    @WithCustomMockUser
    void 히스토리에_감정결과가_없을경우_빈_바디를_전송한다() throws Exception {
        // given
        when(resultRepository.findByMember(any(Long.class))).thenReturn(List.of());
        when(memberRepository.existsById(anyLong())).thenReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/history")
                        .with(user(principalDetails))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithCustomMockUser
    void 감정_결과를_정상적으로_조회한다() throws Exception {
        // given
        AnalysisResult result = ResultFixture.createAnalysisResult();
        when(resultRepository.findById(eq(result.getId()))).thenReturn(Optional.of(result));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/history/result")
                        .param("id", result.getId())
                        .with(user(principalDetails))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(result.getId()))
                .andExpect(jsonPath("$.title").value(result.getTitle()));
    }

    @Test
    @WithCustomMockUser
    void 감정_결과의_이름을_정상적으로_변경한다() throws Exception {
        // given
        AnalysisResult result = ResultFixture.createAnalysisResult();
        ResultUpdateRequest request = new ResultUpdateRequest("새로지은 이름");
        when(resultRepository.findById(eq(result.getId()))).thenReturn(Optional.of(result));
        when(resultRepository.save(any(AnalysisResult.class))).thenReturn(result);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/history/result")
                        .param("id", result.getId())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(principalDetails))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(result.getId() + "의 이름이 변경됐습니다."));
    }

    @Test
    @WithCustomMockUser
    void 요청한_이름이_20자를_초과할_경우_예외를_반환한다() throws Exception {
        // given
        AnalysisResult result = ResultFixture.createAnalysisResult();
        ResultUpdateRequest request = new ResultUpdateRequest("이름이왜이렇게긴지모르겠지만테스트를위해아무이름이나짓는다");
        when(resultRepository.findById(eq(result.getId()))).thenReturn(Optional.of(result));
        when(resultRepository.save(any(AnalysisResult.class))).thenReturn(result);

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/v1/history/result")
                        .param("id", result.getId())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(principalDetails))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이름은 1자 이상, 20자 이하여야 합니다."));
    }

    @Test
    @WithCustomMockUser
    void 감정_결과를_정상적으로_삭제한다() throws Exception {
        // given
        AnalysisResult result1 = ResultFixture.createAnalysisResult();
        when(resultRepository.existsById(eq(result1.getId()))).thenReturn(true);
        doNothing().when(resultRepository).deleteById(eq(result1.getId()));

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/history/result")
                        .param("id", result1.getId())
                        .with(user(principalDetails))
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("삭제에 성공했습니다."));
    }

    @Test
    @WithCustomMockUser
    void 존재하지_않는_감정_결과_삭제시_예외를_반환한다() throws Exception {
        // given
        AnalysisResult result1 = ResultFixture.createAnalysisResult();
        when(resultRepository.existsById(eq(result1.getId()))).thenReturn(false);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/history/result")
                        .param("id", result1.getId())
                        .with(user(principalDetails))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("결과를 찾을 수 없습니다."));
    }
}