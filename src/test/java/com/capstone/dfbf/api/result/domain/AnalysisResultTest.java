package com.capstone.dfbf.api.result.domain;

import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static com.capstone.dfbf.api.fixture.ResultFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AnalysisResultTest {

    @Test
    void 유사도_필압_기울기_검증물사진주소들을_모두_업데이트한다() {
        // given
        AnalysisResult rawResult = createNullAnalysisResult();
        AppraisalResponse response = createAppraisalResponse();

        // when
        AnalysisResult updatedResult = rawResult.updateWith(response);

        // then
        assertThat(updatedResult.getInclination()).isEqualTo(response.inclination());
        assertThat(updatedResult.getPressure()).isEqualTo(response.pressure());
        assertThat(updatedResult.getSimilarity()).isEqualTo(response.similarity());
        assertThat(updatedResult.getVerificationImgUrl()).isEqualTo(response.verificationImgUrl());
    }

    @Test
    void 필압만_업데이트한다() {
        /* TODO
            기존 AppraisalResponse를 이용한 updateWith 대신
            별도의 DTO를 활용하는 메서드 추가로 개발해야함
        */

        // given

        // when

        // then

    }
}