package com.capstone.dfbf.global.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3Properties {

    public static String VERIFICATION_PREFIX;
    public static String COMPARISON_PREFIX;

    public S3Properties(
            @Value("${spring.cloud.aws.s3.verification-prefix}") String verificationPrefix,
            @Value("${spring.cloud.aws.s3.comparison-prefix}") String comparisonPrefix
    ) {
        VERIFICATION_PREFIX = verificationPrefix;
        COMPARISON_PREFIX = comparisonPrefix;
    }
}
