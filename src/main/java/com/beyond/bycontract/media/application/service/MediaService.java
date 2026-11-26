package com.beyond.bycontract.media.application.service;

import com.beyond.bycontract.media.application.dto.GetMediaCommand;
import com.beyond.bycontract.media.application.dto.MediaContentResponse;
import com.beyond.bycontract.media.application.dto.UploadMediaCommand;
import com.beyond.bycontract.media.application.dto.UploadMediaResponse;
import com.beyond.bycontract.media.domain.exception.MediaNotFoundException;
import com.beyond.bycontract.media.domain.model.Media;
import com.beyond.bycontract.media.domain.repository.MediaRepository;
import com.beyond.bycontract.shared.utils.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class MediaService {

    private final MediaRepository repository;
    private final String apiBaseUrl;

    public MediaService(
            MediaRepository repository,
            @Value("${app.api-base-url}") String apiBaseUrl
    ) {
        this.repository = repository;
        this.apiBaseUrl = apiBaseUrl;
    }

    public UploadMediaResponse uploadMedia(UploadMediaCommand command) {
        Media media = Media.create(
                command.content(),
                command.contentType(),
                command.size(),
                command.idRequester()
        );

        repository.save(media);

        String viewUrl = UriComponentsBuilder.fromUriString(apiBaseUrl)
                .path("/api/v1/media/view")
                .queryParam("key", media.getKey())
                .toUriString();

        return new UploadMediaResponse(media.getKey(), viewUrl);
    }

    public MediaContentResponse getMediaByKey(GetMediaCommand command) {
        UUID ownerId = Media.extractOwnerIdFromKey(command.key());
        AuthorizationUtils.verifyOwnership(ownerId, command.idRequester());

        Media media = repository.findByKey(command.key())
                .orElseThrow(() -> new MediaNotFoundException(command.key()));

        return new MediaContentResponse(media.getContent(), media.getContentType());
    }
}
