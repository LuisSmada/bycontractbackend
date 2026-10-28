package com.beyond.bycontract.user.presentation.dto;

import com.beyond.bycontract.user.application.dto.CreateUserCommand;

public record CreateUserRequest (
        String firstName,
        String lastName,
        String email,
        String password
) {
    public CreateUserCommand toCommand() {
        return new CreateUserCommand(
                this.firstName(),
                this.lastName(),
                this.email(),
                this.password()
        );
    }
}
