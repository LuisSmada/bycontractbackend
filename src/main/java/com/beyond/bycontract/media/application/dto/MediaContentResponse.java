package com.beyond.bycontract.media.application.dto;

public record MediaContentResponse(
        byte[] content,
        String contentType
) {}
