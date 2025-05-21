package com.capstone.dfbf.api.fixture;

import com.capstone.dfbf.api.personality.dto.PersonalityRequest;
import com.capstone.dfbf.api.personality.dto.PersonalityResponse;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalAIResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResultFixture {

    private final static LocalDate createdAt = LocalDate.of(2024, 4, 11);
    private final static String id = "01JRSYFFCD6R6C88JJFA0JTZPB";

    public static AppraisalRequest createAppraisalRequest() {
        final String verification = "https://ggzz-img.s3.ap-northeast-2.amazonaws.com/verification/%E1%84%80%E1%85%A5%E1%86%B7%E1%84%8C%E1%85%B3%E1%86%BC%E1%84%86%E1%85%AE%E1%86%AF1.png";
        final List<String> comparisons = new ArrayList<>(List.of(
                "https://ggzz-img.s3.ap-northeast-2.amazonaws.com/comparison/%E1%84%83%E1%85%A2%E1%84%8C%E1%85%A9%E1%84%86%E1%85%AE%E1%86%AF1.jpeg",
                "https://ggzz-img.s3.ap-northeast-2.amazonaws.com/comparison/%E1%84%83%E1%85%A2%E1%84%8C%E1%85%A9%E1%84%86%E1%85%AE%E1%86%AF2.jpeg",
                "https://ggzz-img.s3.ap-northeast-2.amazonaws.com/comparison/%E1%84%83%E1%85%A2%E1%84%8C%E1%85%A9%E1%84%86%E1%85%AE%E1%86%AF3.jpeg"
        ));
        return new AppraisalRequest(verification, comparisons);
    }

    public static AppraisalAIResponse createAppraisalResponse() {
        return AppraisalAIResponse.builder()
                .inclination(1.223)
                .similarity(1.3)
                .pressure(32.1)
                .verificationImageUrl("https://ggzz.s3.ap-northeast-2.amazonaws.com/verification-img/%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B52.png")
                .build();
    }

    public static AnalysisResult createAnalysisResultWOId() {
        return AnalysisResult.builder()
                .inclination(1.223)
                .similarity(1.3)
                .pressure(32.1)
                .verificationImgUrl("https://ggzz.s3.ap-northeast-2.amazonaws.com/verification-img/%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B52.png")
                .build();
    }

    public static AnalysisResult createAnalysisResult() {
        return AnalysisResult.builder()
                .id(id)
                .title("테스트 결과")
                .inclination(1.223)
                .similarity(1.3)
                .pressure(32.1)
                .verificationImgUrl("https://ggzz.s3.ap-northeast-2.amazonaws.com/verification-img/%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B52.png")
                .build();
    }

    public static AnalysisResult createNullAnalysisResult() {
        return AnalysisResult.builder()
                .id(id)
                .build();
    }

    public static PersonalityRequest createPersonalityRequest() {
        return PersonalityRequest.builder()
                .imageUrl("https://ggzz-img.s3.ap-northeast-2.amazonaws.com/personality/test.png")
                .build();
    }
}
