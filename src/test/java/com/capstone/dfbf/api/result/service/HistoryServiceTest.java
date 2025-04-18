package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.member.Member;
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

import java.util.List;

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

    private AnalysisResult result;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).email("hong123@gmail.com").nickname("hong").build();
        result = ResultFixture.createAnalysisResult();
        result.update(member);
    }

    @Test
    void 회원_ID를_통해_히스토리_결과_조회를_성공한다() {
        // given
        final long memberId = member.getId();
        when(resultRepository.findByMember(memberId)).thenReturn(List.of(result));

        // when
        List<HistoryResultResponse> responses = historyService.getHistoryByMemberId(memberId);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().id()).isEqualTo(result.getId());
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
    void 히스토리에서_결과의_이름을_변경을_성공한다() {
        // given

        // when

        // then
    }
}