package com.capstone.dfbf.api.result.dao;

import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ResultRepositoryTest {

    @Autowired
    private ResultRepository resultRepository;

    @Test
    void ULID_PK_자동_생성을_확인한다() {
        // given
        AnalysisResult result = ResultFixture.createAnalysisResult();

        // when
        AnalysisResult savedResult = resultRepository.save(result);

        // then
        System.out.println(savedResult.getId());
        assertThat(savedResult.getId()).isNotNull();
        assertThat(savedResult.getId()).hasSize(26);
        assertThat(savedResult.getId()).matches("^[0-9A-HJKMNP-TV-Z]{26}$");
    }
}
