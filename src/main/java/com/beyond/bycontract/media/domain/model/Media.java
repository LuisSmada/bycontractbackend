package com.beyond.bycontract.media.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class Media {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024; // 5 Mo
    private static final Map<String, String> ALLOWED_MIME_TYPES = Map.of(
            "image/png", "png",
            "image/jpeg", "jpg",
            "image/webp", "webp"
    );
    private static final Pattern KEY_PATTERN = Pattern.compile(
            "^media/([0-9a-fA-F-]{36})/[0-9a-fA-F-]{36}\\.(png|jpg|webp)$"
    );

    private final String key;
    private final UUID idOwner;
    private final byte[] content;
    private final String contentType;
    private final long size;

    public static Media create(byte[] content, String contentType, long size, UUID idOwner) {
        if (idOwner == null) {
            throw new IllegalArgumentException("L'identifiant propriétaire est obligatoire.");
        }
        if (content == null || content.length == 0 || size <= 0) {
            throw new IllegalArgumentException("Le fichier est vide.");
        }
        if (size > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("La taille maximale autorisée est de 5 Mo.");
        }

        String normalizedMime = contentType != null ? contentType.toLowerCase() : null;
        String extension = normalizedMime != null ? ALLOWED_MIME_TYPES.get(normalizedMime) : null;
        if (extension == null) {
            throw new IllegalArgumentException("Format non supporté. Formats autorisés : PNG, JPEG, WEBP.");
        }

        String key = "media/%s/%s.%s".formatted(idOwner, UUID.randomUUID(), extension);
        return new Media(key, idOwner, content, normalizedMime, size);
    }

    public static Media reconstitute(String key, byte[] content, String contentType) {
        UUID ownerId = extractOwnerIdFromKey(key);
        long size = content != null ? content.length : 0L;
        return new Media(key, ownerId, content, contentType, size);
    }

    public static UUID extractOwnerIdFromKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("La clé média est invalide.");
        }
        Matcher matcher = KEY_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Format de clé média invalide.");
        }
        return UUID.fromString(matcher.group(1));
    }
}