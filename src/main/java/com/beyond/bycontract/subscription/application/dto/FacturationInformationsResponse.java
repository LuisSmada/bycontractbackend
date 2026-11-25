package com.beyond.bycontract.subscription.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FacturationInformationsResponse(
        @NotBlank(message = "Le nom ou la raison sociale est requis")
        String name,

        @NotBlank(message = "L'e-mail de facturation est requis")
        @Email(message = "Format d'e-mail invalide")
        String email,

        @NotBlank(message = "L'adresse est requise")
        String line1,

        String line2, // Complément d'adresse (facultatif)

        @NotBlank(message = "La ville est requise")
        String city,

        @NotBlank(message = "Le code postal est requis")
        String postalCode,

        @NotBlank(message = "Le code pays est requis")
        @Size(min = 2, max = 2, message = "Format ISO à 2 lettres attendu (ex: FR, BE, CH)")
        String country
) {
}
