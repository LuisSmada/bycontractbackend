package com.beyond.bycontract.adapter.infrastructure.repository.file;

import com.beyond.bycontract.adapter.infrastructure.entity.FileEntity;
import com.beyond.bycontract.adapter.infrastructure.mapper.FileMapper;
import com.beyond.bycontract.domain.model.File;
import com.beyond.bycontract.domain.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaFileRepository implements FileRepository {

    @Autowired
    private SprintgDataFileRepository sprintgDataFileRepository;

    @Override
    public File createFile(File file) {
        FileEntity fileEntity = FileMapper.domainToEntity(file);
        return FileMapper.entityToDomain(sprintgDataFileRepository.save(fileEntity));
    }
}
