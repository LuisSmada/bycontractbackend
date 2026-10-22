package com.beyond.bycontract.company.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParticularStakeholderResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {}
