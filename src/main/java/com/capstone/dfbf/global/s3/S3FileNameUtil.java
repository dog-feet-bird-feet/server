package com.capstone.dfbf.global.s3;

import java.util.UUID;

public class S3FileNameUtil {
    public static String renameFilename(String fileName){
        return UUID.randomUUID() + "_" + fileName;
    }
}
