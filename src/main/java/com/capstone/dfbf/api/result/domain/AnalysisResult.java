package com.capstone.dfbf.api.result.domain;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnalysisResult extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(name = "ulid-generator", strategy = "com.capstone.dfbf.api.result.domain.UlidGenerator")
    private String id;
    private Double similarity;
    private Double pressure;
    private Double inclination;
    private String verificationImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private AnalysisResult analysisResult;

    public AnalysisResult updateWith(final AppraisalResponse response) {
        return AnalysisResult.builder()
                .id(this.id)
                .member(this.member)
                .similarity(response.similarity())
                .inclination(response.inclination())
                .pressure(response.pressure())
                .verificationImgUrl(response.verificationImgUrl())
                .build();
    }
}
