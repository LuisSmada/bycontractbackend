package com.beyond.bycontract.media.domain.repository;

import com.beyond.bycontract.media.domain.model.Media;

import java.util.Optional;

public interface MediaRepository {
    void save(Media media);
    Optional<Media> findByKey(String key);
}
