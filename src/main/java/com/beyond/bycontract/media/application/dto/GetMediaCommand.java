package com.beyond.bycontract.media.application.dto;

import java.util.UUID;

public record GetMediaCommand(
        UUID idRequester,
        String key
) {}
