package com.capstone.dfbf.api.result;

import com.capstone.dfbf.api.annotation.WithCustomMockUser;
import com.capstone.dfbf.api.fixture.ResultFixture;
import com.capstone.dfbf.api.injector.MockitoBeanInjector;
import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
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

import static org.hamcrest.Matchers.empty;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppraisalIntegrationTest {

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

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        generateToken();
    }
    
    @Test
    @WithCustomMockUser
    void AI_서버에_분석을_의뢰한다() throws Exception {
        final var request = ResultFixture.createAppraisalRequest();

        RestAssured
                .given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .body(request)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/appraisal")
                .then().log().all()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .body("", empty())
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

        if(memberRepository.findMemberByEmail("hong1234@gmail.com").isEmpty()) {
            member = memberRepository.save(member);
            System.out.println(member.getId());
        }

        final AuthenticatedMember authenticatedMember = AuthenticatedMember.from(member);
        accessToken = jwtProvider.generateAccessToken(authenticatedMember).token();
    }
}