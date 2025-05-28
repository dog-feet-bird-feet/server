package com.capstone.dfbf.api.result.domain;

import com.capstone.dfbf.api.evidence.domain.EvidenceGroup;
import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.result.dto.AppraisalAIResponse;
import com.capstone.dfbf.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnalysisResult extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(name = "ulid-generator", strategy = "com.capstone.dfbf.api.result.domain.UlidGenerator")
    private String id;

    @Column(nullable = false)
    @Builder.Default
    private String title = "제목없음";
    private Double similarity;
    private Double pressure;
    private Double inclination;
    private String verificationImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private EvidenceGroup evidenceGroup;

    public AnalysisResult updateWith(final AppraisalAIResponse response) {
        return AnalysisResult.builder()
                .id(this.id)
                .member(this.member)
                .similarity(response.similarity())
                .inclination(response.inclination())
                .pressure(response.pressure())
                .verificationImgUrl(response.verificationImageUrl())
                .build();
    }

    public AnalysisResult updateWith(final String title) {
        return AnalysisResult.builder()
                .id(this.id)
                .title(title)
                .member(this.member)
                .similarity(this.similarity)
                .inclination(this.inclination)
                .pressure(this.pressure)
                .verificationImgUrl(this.verificationImgUrl)
                .build();
    }

    public void update(final Member member) {
        setMember(member);
    }
}
