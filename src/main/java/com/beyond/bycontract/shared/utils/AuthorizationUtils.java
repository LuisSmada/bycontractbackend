package com.beyond.bycontract.shared.utils;

import org.springframework.security.access.AccessDeniedException;

import java.util.UUID;

public class AuthorizationUtils {
    private AuthorizationUtils() {}

    /**
     * Vérifie que l'utilisateur qui fait la requête est bien le propriétaire de la ressource.
     * @param resourceOwnerId L'ID du créateur de la ressource en BDD
     * @param requesterId L'ID de l'utilisateur connecté qui fait la requête
     */
    public static void verifyOwnership(UUID resourceOwnerId, UUID requesterId) {
        if (resourceOwnerId == null || requesterId == null || !resourceOwnerId.equals(requesterId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à effectuer cette opération.");
        }
    }
}
