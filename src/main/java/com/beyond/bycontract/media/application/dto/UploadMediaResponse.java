package com.beyond.bycontract.media.application.dto;

public record UploadMediaResponse(
        String s3Key,
        String url
) {}
