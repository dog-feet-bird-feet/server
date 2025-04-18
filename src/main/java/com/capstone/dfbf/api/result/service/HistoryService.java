package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.domain.History;
import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class HistoryService {

    private final ResultRepository resultRepository;

    public List<HistoryResultResponse> getHistoryByMemberId(final Long memberId) {
        List<AnalysisResult> results = resultRepository.findByMember(memberId);
        History history = new History(results);
        return HistoryResultResponse.from(history);
    }
}
