package com.capstone.dfbf.api.personality.dto;

import com.capstone.dfbf.api.personality.domain.Personality;
import com.capstone.dfbf.api.personality.domain.Traits;
import lombok.Builder;

@Builder
public record PersonalityResponse(
        Long id,
        Traits traits,
        String type,
        String description
) {

    public static PersonalityResponse from(Personality personality) {
        return PersonalityResponse.builder()
                .id(personality.getId())
                .traits(personality.getTraits())
                .type(personality.getType())
                .description(personality.getDescription())
                .build();
    }
}
