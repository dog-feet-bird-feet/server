package com.capstone.dfbf.api.result.service;

import com.capstone.dfbf.api.member.repository.MemberRepository;
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

import static com.capstone.dfbf.global.exception.error.ErrorCode.RESULT_NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
@Service
public class HistoryService {

    private final ResultRepository resultRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<HistoryResultResponse> getHistoryByMemberId(final Long memberId) {
        if(!isMemberExist(memberId)) {
            throw BaseException.from(ErrorCode.MEMBER_NOT_FOUND);
        }

        List<AnalysisResult> results = resultRepository.findByMemberCreatedAtDesc(memberId);
        History history = new History(results);
        return HistoryResultResponse.from(history);
    }

    @Transactional(readOnly = true)
    public ResultResponse getResultById(final String resultId) {
        AnalysisResult result = findById(resultId);
        return ResultResponse.from(result);
    }

    @Transactional
    public String updateResultName(final String resultId, final String newName) {
        AnalysisResult result = findById(resultId);
        result = result.updateWith(newName);
        resultRepository.save(result);
        return result.getId();
    }

    @Transactional
    public void deleteResult(final String resultId) {
        if(!isResultExist(resultId)) {
            throw BaseException.from(RESULT_NOT_FOUND);
        }

        resultRepository.deleteById(resultId);
    }

    private AnalysisResult findById(final String resultId) {
        return resultRepository.findById(resultId).orElseThrow(() -> BaseException.from(RESULT_NOT_FOUND));
    }

    private boolean isResultExist(final String resultId) {
        return resultRepository.existsById(resultId);
    }

    private boolean isMemberExist(final Long memberId) {
        return memberRepository.existsById(memberId);
    }
}
