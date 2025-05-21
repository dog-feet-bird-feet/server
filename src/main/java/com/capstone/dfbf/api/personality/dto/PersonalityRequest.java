package com.capstone.dfbf.api.personality.dto;

import lombok.Builder;

@Builder
public record PersonalityRequest(
        String imageUrl
) {
}
