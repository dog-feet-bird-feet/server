package com.capstone.dfbf.api.injector;

import com.capstone.dfbf.api.result.dao.ResultRepository;
import com.capstone.dfbf.global.security.filter.JwtFilter;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class MockitoBeanInjector {

    @MockitoBean
    protected ResultRepository resultRepository;
    @MockitoBean
    protected JwtFilter jwtFilter;
    @MockitoBean
    protected JwtProvider jwtProvider;
}
