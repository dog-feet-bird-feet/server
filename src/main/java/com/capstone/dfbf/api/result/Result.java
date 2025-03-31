package com.capstone.dfbf.api.result;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.global.base.BaseEntity;
import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ulid = UlidCreator.getUlid().toString();
    private Double similarity;
    private Double pressure;
    private Double inclination;
    private String verificationImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
