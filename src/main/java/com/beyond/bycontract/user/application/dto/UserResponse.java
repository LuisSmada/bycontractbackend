package com.beyond.bycontract.user.application.dto;

import java.time.LocalDateTime;

public record UserResponse(
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}
