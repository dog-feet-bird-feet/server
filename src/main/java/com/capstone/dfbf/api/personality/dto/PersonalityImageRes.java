package com.capstone.dfbf.api.personality.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonalityImageRes {
    private String personalityUrl;

    public static PersonalityImageRes from(String personalityUrl) {
        return new PersonalityImageRes(personalityUrl);
    }
}
