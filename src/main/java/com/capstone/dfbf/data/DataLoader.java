package com.capstone.dfbf.data;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.api.result.domain.AnalysisResult;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member = Member.builder()
                .email("sungwon326@naver.com")
                .nickname("성원")
                .build();
        memberRepository.save(member);

        AnalysisResult result = AnalysisResult.builder()
                .ulid(UlidCreator.getUlid().toString())
                .member(member)
                .build();
        resultRepository.save(result);
    }
}
