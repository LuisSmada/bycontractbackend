package com.beyond.bycontract.media.application.dto;

import java.util.UUID;

public record UploadMediaCommand(
        UUID idRequester,
        byte[] content,
        String contentType,
        long size
) {}