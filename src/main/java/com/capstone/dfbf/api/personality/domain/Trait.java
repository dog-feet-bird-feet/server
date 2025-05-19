package com.capstone.dfbf.api.personality.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Trait {

    private String score;
    private String detail;

}
