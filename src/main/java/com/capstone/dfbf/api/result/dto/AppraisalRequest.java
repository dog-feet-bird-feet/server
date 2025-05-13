package com.capstone.dfbf.api.result.dto;

import java.util.List;

public record AppraisalRequest(
        String verificationImageUrl,
        List<String> comparisonImageUrls
) {

}
