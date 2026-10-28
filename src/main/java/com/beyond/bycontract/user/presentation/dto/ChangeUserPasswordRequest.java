package com.beyond.bycontract.user.presentation.dto;

import com.beyond.bycontract.user.application.dto.ChangeUserPasswordCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChangeUserPasswordRequest(
        @NotBlank(message = "L'ancien mot de passe est requis")
        String oldPassword,
        @NotBlank(message = "Le nouveau mot de passe est requis")
        @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
        String newPassword
) {
        public ChangeUserPasswordCommand toCommand(UUID idUser) {
                return new ChangeUserPasswordCommand(
                        idUser, this.oldPassword(), this.newPassword()
                );
        }
}
