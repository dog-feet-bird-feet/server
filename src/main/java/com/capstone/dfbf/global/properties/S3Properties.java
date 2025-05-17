package com.capstone.dfbf.global.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3Properties {

    public static String VERIFICATION_PREFIX;
    public static String COMPARISON_PREFIX;
    public static String PERSONALITY_PREFIX;
    public static String BUCKET_NAME;
    public static Integer FILE_LIMIT_SIZE = 10 * 1024 * 1024;

    public S3Properties(
            @Value("${spring.cloud.aws.s3.verification-prefix}") String verificationPrefix,
            @Value("${spring.cloud.aws.s3.comparison-prefix}") String comparisonPrefix,
            @Value("${spring.cloud.aws.s3.personality-prefix}") String personalityPrefix,
            @Value("${spring.cloud.aws.s3.bucket}") String bucket
    ) {
        VERIFICATION_PREFIX = verificationPrefix;
        COMPARISON_PREFIX = comparisonPrefix;
        PERSONALITY_PREFIX = personalityPrefix;
        BUCKET_NAME = bucket;
    }
}
