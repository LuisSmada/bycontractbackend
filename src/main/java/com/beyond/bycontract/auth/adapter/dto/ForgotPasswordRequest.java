package com.beyond.bycontract.auth.adapter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(
        @NotBlank(message = "L'email est requis")
        @Email(message = "Le format de l'email est invalide")
        String email
) {}

