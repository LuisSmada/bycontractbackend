package com.beyond.bycontract.auth.adapter.dto;

import java.util.UUID;

public record GetCurrentUserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String role
) {
}
