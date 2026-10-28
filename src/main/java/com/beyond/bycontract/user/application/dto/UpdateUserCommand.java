package com.beyond.bycontract.user.application.dto;

import java.util.UUID;

public record UpdateUserCommand(
        UUID idUser,
        UUID idRequester,
        String firstName,
        String lastName,
        String email
) {
}
