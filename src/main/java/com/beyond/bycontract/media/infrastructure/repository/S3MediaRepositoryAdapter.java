package com.beyond.bycontract.media.infrastructure.repository;

import com.beyond.bycontract.media.domain.model.Media;
import com.beyond.bycontract.media.domain.repository.MediaRepository;
import com.beyond.bycontract.shared.infrastructure.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class S3MediaRepositoryAdapter implements MediaRepository {

    private final S3StorageService s3StorageService;

    @Override
    public void save(Media media) {
        s3StorageService.uploadBytes(
                media.getKey(),
                media.getContent(),
                media.getContentType()
        );
    }

    @Override
    public Optional<Media> findByKey(String key) {
        return s3StorageService.downloadBytes(key)
                .map(obj -> Media.reconstitute(key, obj.content(), obj.contentType()));
    }
}
