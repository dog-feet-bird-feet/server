package com.capstone.dfbf.api.result.domain;

import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HistoryTest {

    private History history;

    @Mock
    private AnalysisResult result1, result2, result3;

    @BeforeEach
    void setUp() {
        history = new History();
        history.addResult(result1);
        history.addResult(result2);
        history.addResult(result3);
    }

    @Test
    void ID_내림차순으로_히스토리를_정렬한다() {
        // given
        when(result1.getId()).thenReturn(UlidCreator.getUlid(1).toString());
        when(result2.getId()).thenReturn(UlidCreator.getUlid(2).toString());
        when(result3.getId()).thenReturn(UlidCreator.getUlid(3).toString());

        // when
        history.sortResultsByIdDesc();

        // then
        assertThat(history.getResults()).hasSize(3);
        assertThat(history.getResults().getFirst().getId()).isEqualTo(result3.getId());
        assertThat(history.getResults().get(1).getId()).isEqualTo(result2.getId());
        assertThat(history.getResults().get(2).getId()).isEqualTo(result1.getId());
    }

    @Test
    void 생성일자_내림차순으로_히스토리를_정렬한다() {
        // given
        when(result1.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 4, 17, 0, 0));
        when(result2.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 4, 18, 0, 0));
        when(result3.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 4, 19, 0, 0));

        // when
        history.sortResultsByCreatedAtDesc();

        // then
        assertThat(history.getResults()).hasSize(3);
        assertThat(history.getResults().getFirst().getCreatedAt()).isEqualTo(result3.getCreatedAt());
        assertThat(history.getResults().get(1).getCreatedAt()).isEqualTo(result2.getCreatedAt());
        assertThat(history.getResults().get(2).getCreatedAt()).isEqualTo(result1.getCreatedAt());
    }
}