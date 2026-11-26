package com.beyond.bycontract.media;

public record MediaUploadResponse(
        String s3Key,
        String url
) {
}
