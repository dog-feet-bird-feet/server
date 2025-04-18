package com.capstone.dfbf.api.result.controller;

import com.capstone.dfbf.api.annotation.WithCustomMockUser;
import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.injector.MockitoBeanInjector;
import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import com.capstone.dfbf.api.result.service.HistoryService;
import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class HistoryControllerTest extends MockitoBeanInjector {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected HistoryService historyService;

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
    void testGetHistory() throws Exception {
        // given
        AnalysisResult result1 = ResultFixture.createAnalysisResult();
        AnalysisResult result2 = ResultFixture.createAnalysisResult();
        when(resultRepository.findByMember(any(Long.class))).thenReturn(List.of(result1, result2));

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
                .andExpect(jsonPath("$[0].name").value(result1.getName()))
                .andExpect(jsonPath("$[0].verificationImgUrl").value(result1.getVerificationImgUrl()))

                .andExpect(jsonPath("$[1].id").value(result2.getId()))
                .andExpect(jsonPath("$[1].name").value(result2.getName()))
                .andExpect(jsonPath("$[1].verificationImgUrl").value(result2.getVerificationImgUrl()));
    }
}