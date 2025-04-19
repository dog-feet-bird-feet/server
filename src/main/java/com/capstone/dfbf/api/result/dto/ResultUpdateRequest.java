package com.capstone.dfbf.api.result.dto;

import jakarta.validation.constraints.Size;

public record ResultUpdateRequest(
        @Size(min = 1, max = 20, message = "이름은 1자 이상, 20자 이하여야 합니다.")
        String newName
) {

}
