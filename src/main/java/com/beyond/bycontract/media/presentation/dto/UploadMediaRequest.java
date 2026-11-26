package com.beyond.bycontract.media.presentation.dto;

import com.beyond.bycontract.media.application.dto.UploadMediaCommand;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;

public record UploadMediaRequest(
        @NotNull(message = "Le fichier est obligatoire.")
        MultipartFile file
) {
    public UploadMediaCommand toCommand(@NotNull UUID idRequester) {
        try {
            return new UploadMediaCommand(
                    idRequester,
                    this.file.getBytes(),
                    this.file.getContentType(),
                    this.file.getSize()
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de lire le fichier envoyé.", e);
        }
    }
}
