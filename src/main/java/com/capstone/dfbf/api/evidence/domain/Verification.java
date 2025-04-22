package com.capstone.dfbf.api.evidence.domain;

import com.capstone.dfbf.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Verification extends BaseEntity implements Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgUrl;

    public Verification(String key) {
        this.imgUrl = key;
    }

    @Override
    public Evidence updateImgUrl(String imgUrl) {
        return Comparison.builder()
                .id(this.id)
                .imgUrl(imgUrl)
                .build();
    }

    public static Verification from(String key) {
        return new Verification(key);
    }
}
