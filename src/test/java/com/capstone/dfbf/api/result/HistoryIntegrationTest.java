package com.capstone.dfbf.api.result;

import com.capstone.dfbf.api.annotation.WithCustomMockUser;
import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.ResultUpdateRequest;
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

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HistoryIntegrationTest {

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
    private AnalysisResult result1;
    private AnalysisResult result2;

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
                        .body("[0].createdAt", equalTo(result1.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))))
                        .body("[0].verificationImgUrl", containsString("https://"))
                        .body("[1].id", notNullValue())
                        .body("[1].createdAt", equalTo(result2.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))))
                        .body("[1].verificationImgUrl", containsString("https://"))
                .extract();
    }

    @Test
    void 특정_결과를_조회한다() {
        createAndSaveResult();

        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .param("id", result1.getId())
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/history/result")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .body("id", equalTo(result1.getId()))
                        .body("similarity", equalTo(1.3F))
                        .body("pressure", equalTo(32.1F))
                        .body("inclination", equalTo(1.223F))
                        .body("createdAt", equalTo(result1.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))))
                        .body("verificationImgUrl", equalTo(result1.getVerificationImgUrl()))
                .extract();
    }

    @Test
    void 잘못된_결과를_조회한다() {
        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .param("id", "Wrong Result ID")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/v1/history/result")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("message", equalTo("결과를 찾을 수 없습니다."))
                .extract();
    }

    @Test
    void 결과의_이름을_변경한다() {
        createAndSaveResult();
        ResultUpdateRequest request = new ResultUpdateRequest("새로지은 이름");

        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .param("id", result1.getId())
                    .body(request)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/api/v1/history/result")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .body(containsString("의 이름이 변경됐습니다."))
                .extract();
    }

    @Test
    void 잘못된_결과ID로_이름_변경시_실패한다() {
        ResultUpdateRequest request = new ResultUpdateRequest("새로지은 정상적인 이름");

        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .param("id", "Wrong Result ID")
                    .body(request)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/api/v1/history/result")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("message", equalTo("결과를 찾을 수 없습니다."))
                .extract();
    }

    @Test
    void 결과를_삭제한다() {
        createAndSaveResult();

        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .param("id", result1.getId())
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/v1/history/result")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .body(equalTo("삭제에 성공했습니다."))
                .extract();
    }

    @Test
    void 잘못된_결과ID로_삭제에_실패한다() {
        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .param("id", "Wrong Result ID")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/v1/history/result")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("message", equalTo("결과를 찾을 수 없습니다."))
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
            member = memberRepository.save(member);
        final AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        accessToken = jwtProvider.generateAccessToken(authenticatedMember).token();
    }

    private void createAndSaveResult() {
        result1 = ResultFixture.createAnalysisResultWOId();
        result1.update(member);
        result2 = ResultFixture.createAnalysisResultWOId();
        result2.update(member);

        result1 = resultRepository.save(result1);
        result2 = resultRepository.save(result2);
    }
}
