package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import com.capstone.dfbf.global.exception.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @InjectMocks
    private HistoryService historyService;

    @Mock
    private ResultRepository resultRepository;
    @Mock
    private MemberRepository memberRepository;

    private AnalysisResult result;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).email("hong123@gmail.com").nickname("hong").build();
        result = ResultFixture.createAnalysisResult();
        result.update(member);
    }

    @Test
    void 회원ID로_히스토리_결과_조회를_성공한다() {
        // given
        final long memberId = member.getId();
        when(resultRepository.findByMemberCreatedAtDesc(memberId)).thenReturn(List.of(result));
        when(memberRepository.existsById(anyLong())).thenReturn(true);

        // when
        List<HistoryResultResponse> responses = historyService.getHistoryByMemberId(memberId);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().id()).isEqualTo(result.getId());
    }

    @Test
    void 잘못된_회원ID로_히스토리_결과_조회시_예외를_반환한다() {
        // given
        final long memberId = 404;
        when(memberRepository.existsById(memberId)).thenReturn(false);

        // when

        // then
        assertThatThrownBy(() -> historyService.getHistoryByMemberId(memberId))
                .isInstanceOf(BaseException.class)
                .hasMessage("회원이 없습니다.");
    }

    @Test
    void 히스토리에서_결과삭제를_성공한다() {
        // given
        when(resultRepository.existsById(eq(result.getId()))).thenReturn(true);
        doNothing().when(resultRepository).deleteById(eq(result.getId()));

        // when
        historyService.deleteResult(result.getId());

        // then
        verify(resultRepository).deleteById(eq(result.getId()));
    }

    @Test
    void 삭제하려는_결과가_존재하지_않는경우_예외를_반환한다() {
        // given
        when(resultRepository.existsById(eq(result.getId()))).thenReturn(false);

        // when

        // then
        assertThatThrownBy(() -> historyService.deleteResult(result.getId()))
                .hasMessage("결과를 찾을 수 없습니다.")
                .isInstanceOf(BaseException.class);
    }

    @Test
    void 결과ID를_통해_결과_조회를_성공한다() {
        // given
        final String resultId = result.getId();
        when(resultRepository.findById(eq(resultId))).thenReturn(Optional.ofNullable(result));

        // when
        ResultResponse response = historyService.getResultById(resultId);

        // then
        assertThat(response.id()).isEqualTo(result.getId());
        assertThat(response.similarity()).isEqualTo(result.getSimilarity());
        assertThat(response.pressure()).isEqualTo(result.getPressure());
        assertThat(response.inclination()).isEqualTo(result.getInclination());
        assertThat(response.verificationImgUrl()).isEqualTo(result.getVerificationImgUrl());
        assertThat(response.createdAt()).isEqualTo(result.getCreatedAt());
    }

    @Test
    void 잘못된_결과ID로_결과_조회시_예외를_반환한다() {
        // given
        final String resultId = "Incorrect resultId";
        when(resultRepository.findById(eq(resultId))).thenReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> historyService.getResultById(resultId))
                .isInstanceOf(BaseException.class)
                .hasMessage("결과를 찾을 수 없습니다.");
    }

    @Test
    void 히스토리에서_결과의_제목_변경을_성공한다() {
        // given
        final String resultId = result.getId();
        final String newName = "새롭게 변경된 결과";
        when(resultRepository.findById(eq(resultId))).thenReturn(Optional.of(result));
        when(resultRepository.save(any(AnalysisResult.class))).thenReturn(result);

        // when
        String returnedId = historyService.updateResultName(resultId, newName);

        // then
        assertThat(returnedId).isEqualTo(resultId);
    }

    @Test
    void 잘못된_결과ID로_결과_제목_수정시_예외를_반환한다() {
        // given
        final String resultId = "Incorrect resultId";
        final String newName = "새롭게 변경된 결과";
        when(resultRepository.findById(eq(resultId))).thenReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> historyService.updateResultName(resultId, newName))
                .isInstanceOf(BaseException.class)
                .hasMessage("결과를 찾을 수 없습니다.");
    }
}
