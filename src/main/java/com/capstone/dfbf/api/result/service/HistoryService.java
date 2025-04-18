package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.capstone.dfbf.api.result.domain.History;
import com.capstone.dfbf.api.result.dto.HistoryResultResponse;
import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class HistoryService {

    private final ResultRepository resultRepository;

    @Transactional(readOnly = true)
    public List<HistoryResultResponse> getHistoryByMemberId(final Long memberId) {
        List<AnalysisResult> results = resultRepository.findByMember(memberId);
        History history = new History(results);
        return HistoryResultResponse.from(history);
    }

    @Transactional
    public void deleteResult(final String resultId) {
        if(!isResultExist(resultId)) {
            throw BaseException.from(ErrorCode.RESULT_NOT_FOUND);
        }

        resultRepository.deleteById(resultId);
    }

    private boolean isResultExist(final String resultId) {
        return resultRepository.existsById(resultId);
    }
}
