package com.capstone.dfbf.api.injector;

import com.capstone.dfbf.api.evidence.service.EvidenceService;
import com.capstone.dfbf.api.member.repository.MemberRepository;
import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.global.config.S3Config;
import com.capstone.dfbf.global.properties.S3Properties;
import com.capstone.dfbf.global.security.filter.JwtFilter;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

public class MockitoBeanInjector {

    @MockitoBean
    protected ResultRepository resultRepository;
    @MockitoBean
    protected MemberRepository memberRepository;
    @MockitoBean
    protected JwtFilter jwtFilter;
    @MockitoBean
    protected JwtProvider jwtProvider;

    // S3
    @MockitoBean
    protected S3Config s3Config;
    @MockitoBean
    protected S3Client s3Client;
    @MockitoBean
    protected S3Presigner s3Presigner;
    @MockitoBean
    protected EvidenceService evidenceService;
    @MockitoBean
    protected S3Properties s3Properties;
    @MockitoBean
    protected S3ClientBuilder s3ClientBuilder;
}
