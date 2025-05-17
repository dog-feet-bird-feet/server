package com.capstone.dfbf.api.evidence.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ComparisonRes {

    private List<String> comparisonUrls;

    public static ComparisonRes from(List<String> comparisonUrls) {
        return new ComparisonRes(comparisonUrls);
    }
}
