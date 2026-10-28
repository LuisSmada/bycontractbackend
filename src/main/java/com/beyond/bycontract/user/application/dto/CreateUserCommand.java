package com.beyond.bycontract.user.application.dto;

public record CreateUserCommand(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
