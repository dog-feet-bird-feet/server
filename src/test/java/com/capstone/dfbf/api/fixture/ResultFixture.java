package com.capstone.dfbf.api.fixture;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.dto.AppraisalRequest;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

import java.time.LocalDate;

public class ResultFixture {

    private final static LocalDate createdAt = LocalDate.of(2024, 4, 11);
    private final static String id = "01JRSYFFCD6R6C88JJFA0JTZPB";

    public static AppraisalRequest createAppraisalRequest() {
        return new AppraisalRequest(id);
    }

    public static AppraisalResponse createAppraisalResponse() {
        return AppraisalResponse.builder()
                .id(id)
                .inclination(1.223)
                .similarity(1.3)
                .pressure(32.1)
                .verificationImgUrl("https://ggzz.s3.ap-northeast-2.amazonaws.com/verification-img/%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B52.png")
                .createdAt(createdAt)
                .build();
    }

    public static AnalysisResult createAnalysisResult() {
        return AnalysisResult.builder()
                .id(id)
                .inclination(1.223)
                .similarity(1.3)
                .pressure(32.1)
                .verificationImgUrl("https://ggzz.s3.ap-northeast-2.amazonaws.com/verification-img/%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B52.png")
                .build();
    }
}
