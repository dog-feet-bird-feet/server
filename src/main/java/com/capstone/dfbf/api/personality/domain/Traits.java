package com.capstone.dfbf.api.personality.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Traits {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "score", column = @Column(name = "size_score")),
            @AttributeOverride(name = "detail", column = @Column(name = "size_detail"))
    })
    private Trait size;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "score", column = @Column(name = "pressure_score")),
            @AttributeOverride(name = "detail", column = @Column(name = "pressure_detail"))
    })
    private Trait pressure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "score", column = @Column(name = "inclination_score")),
            @AttributeOverride(name = "detail", column = @Column(name = "inclination_detail"))
    })
    private Trait inclination;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "score", column = @Column(name = "shape_score")),
            @AttributeOverride(name = "detail", column = @Column(name = "shape_detail"))
    })
    private Trait shape;
}
