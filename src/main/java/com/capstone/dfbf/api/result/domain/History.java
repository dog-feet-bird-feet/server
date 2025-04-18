package com.capstone.dfbf.api.result.domain;

import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class History {

    private final List<AnalysisResult> results;

    public void addResult(AnalysisResult result) {
        results.add(result);
    }

    public void sortResultsByCreatedAtDesc() {
        results.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
    }

    public void sortResultsByIdDesc() {
        results.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
    }
}
