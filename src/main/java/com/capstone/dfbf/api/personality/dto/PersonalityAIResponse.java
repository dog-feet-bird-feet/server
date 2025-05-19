package com.capstone.dfbf.api.personality.dto;

import com.capstone.dfbf.api.personality.domain.Personality;
import com.capstone.dfbf.api.personality.domain.Traits;

public record PersonalityAIResponse(
        Traits traits,
        String type
) {

    public Personality toEntity() {
        return Personality.builder()
                .traits(this.traits)
                .type(this.type)
                .build();
    }
}
