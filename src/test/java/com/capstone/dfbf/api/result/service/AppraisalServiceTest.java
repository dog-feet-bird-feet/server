package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.controller.AppraisalController;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AppraisalServiceTest {

    @Autowired
    private AppraisalController controller;

    // TODO 단위 테스트가 어렵다고 판단하여 보류
    @Test
    void AI_서버에_분석을_의뢰한다() {
        // given
        final Long id = 1L;

        // when

        // then
    }
}