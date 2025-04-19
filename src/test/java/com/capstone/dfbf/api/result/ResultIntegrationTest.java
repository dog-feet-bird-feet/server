package com.capstone.dfbf.api.result;

import com.capstone.dfbf.api.annotation.WithCustomMockUser;
import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.global.oauth2.domain.OAuthProviderType;
import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResultIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ResultRepository resultRepository;

    private String accessToken;
    private Member member;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        generateToken();
    }

    @AfterEach
    void tearDown() {
        resultRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithCustomMockUser
    void 히스토리에서_빈_목록을_조회한다() {
        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/history")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .body("", empty())
                    .extract();
    }

    @Test
    void 히스토리에서_여러_목록을_조회한다() {
        createAndSaveResult();

        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/history")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .body("", hasSize(2))
                        .body("[0].id", notNullValue())
                        .body("[0].createdAt", equalTo("2025-04-19"))
                        .body("[0].verificationImgUrl", containsString("verification-img/"))
                        .body("[1].id", notNullValue())
                        .body("[1].createdAt", equalTo("2025-04-19"))
                        .body("[1].verificationImgUrl", containsString("verification-img/"))
                .extract();
    }

    private void generateToken() {
        member = Member.builder()
                .email("hong1234@gmail.com")
                .nickname("길동")
                .oauthId(null)
                .role(Member.Role.USER)
                .providerType(OAuthProviderType.NAVER)
                .build();
        if(memberRepository.findMemberByEmail("hong1234@gmail.com").isEmpty())
            memberRepository.save(member);
        final AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        accessToken = jwtProvider.generateAccessToken(authenticatedMember).token();
    }

    private void createAndSaveResult() {
        var result1 = ResultFixture.createAnalysisResultWOId();
        result1.update(member);
        var result2 = ResultFixture.createAnalysisResultWOId();
        result2.update(member);

        resultRepository.save(result1);
        resultRepository.save(result2);
    }
}
