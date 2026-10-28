package com.beyond.bycontract.user.application.dto;

import java.util.UUID;

public record ChangeUserPasswordCommand(
        UUID idUser,
        String oldPassword,
        String newPassword
) {
}
