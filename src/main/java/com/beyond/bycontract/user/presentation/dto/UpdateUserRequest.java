package com.beyond.bycontract.user.presentation.dto;

import com.beyond.bycontract.user.application.dto.UpdateUserCommand;

import java.util.UUID;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String email
) {
    public UpdateUserCommand toCommand( UUID idUser, UUID idRequester) {
        return new UpdateUserCommand(
                idUser,
                idRequester,
                this.firstName(),
                this.lastName(),
                this.email()
        );
    }
}
