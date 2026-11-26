package com.beyond.bycontract.media.presentation;

import com.beyond.bycontract.media.application.dto.MediaContentResponse;
import com.beyond.bycontract.media.application.dto.UploadMediaResponse;
import com.beyond.bycontract.media.application.service.MediaService;
import com.beyond.bycontract.media.presentation.dto.GetMediaRequest;
import com.beyond.bycontract.media.presentation.dto.UploadMediaRequest;
import com.beyond.bycontract.shared.utils.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadMediaResponse> uploadMedia(
            @Valid @ModelAttribute UploadMediaRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        UploadMediaResponse response = service.uploadMedia(request.toCommand(currentUser.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/view")
    public ResponseEntity<byte[]> viewMedia(
            @Valid @ModelAttribute GetMediaRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        MediaContentResponse media = service.getMediaByKey(request.toCommand(currentUser.getId()));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.contentType()))
                .cacheControl(CacheControl.maxAge(Duration.ofHours(24)).cachePrivate())
                .header("X-Content-Type-Options", "nosniff")
                .body(media.content());
    }
}