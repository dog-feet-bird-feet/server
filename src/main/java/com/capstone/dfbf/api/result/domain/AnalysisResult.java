package com.capstone.dfbf.api.result.domain;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.result.dto.AppraisalResponse;
import com.capstone.dfbf.global.base.BaseEntity;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnalysisResult extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ulid = UlidCreator.getUlid().toString();
    private Double similarity;
    private Double pressure;
    private Double inclination;
    private String verificationImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public AnalysisResult updateWith(final AppraisalResponse response) {
        return AnalysisResult.builder()
                .id(this.id)
                .ulid(this.ulid)
                .member(this.member)
                .similarity(response.similarity())
                .inclination(response.inclination())
                .pressure(response.pressure())
                .verificationImgUrl(response.verificationImgUrl())
                .build();
    }
}
