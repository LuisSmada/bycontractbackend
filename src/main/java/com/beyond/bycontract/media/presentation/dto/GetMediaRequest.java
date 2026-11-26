package com.beyond.bycontract.media.presentation.dto;

import com.beyond.bycontract.media.application.dto.GetMediaCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetMediaRequest(
        @NotBlank(message = "La clé média est obligatoire.")
        String key
) {
    public GetMediaCommand toCommand(@NotNull UUID idRequester) {
        return new GetMediaCommand(idRequester, this.key);
    }
}
