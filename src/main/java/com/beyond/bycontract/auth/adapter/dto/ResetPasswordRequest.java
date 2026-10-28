package com.beyond.bycontract.auth.adapter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Le token est requis")
        String token,

        @NotBlank(message = "Le nouveau mot de passe est requis")
        @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
        String newPassword
) {}
