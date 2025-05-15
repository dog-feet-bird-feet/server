package com.capstone.dfbf.global.properties;

public class JwtProperties {
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30; // 1달
    public static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = "Bearer ";
}
