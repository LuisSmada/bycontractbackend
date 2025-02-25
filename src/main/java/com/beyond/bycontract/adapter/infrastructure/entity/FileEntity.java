package com.beyond.bycontract.adapter.infrastructure.entity;

import com.beyond.bycontract.domain.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file")
public class FileEntity {
    @Id
    @Column(name="id_file")
    private String idFile;

    @Column(name="file_name")
    private String fileName;

    @Column(name="created_at", updatable=false, insertable=false)
    private LocalDateTime createdAt;

    @Column(name="size")
    private int size;

    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_parent_folder")
    private FolderEntity parentFolder;

    public FileEntity() {}

    public FileEntity(String idFile, String fileName, LocalDateTime createdAt, int size, UserEntity user, FolderEntity parentFolder) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.createdAt = createdAt;
        this.size = size;
        this.user = user;
        this.parentFolder = parentFolder;
    }

    public String getIdFile() {
        return idFile;
    }

    public void setIdFile(String idFile) {
        this.idFile = idFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public FolderEntity getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FolderEntity parentFolder) {
        this.parentFolder = parentFolder;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "idFile=" + idFile +
                ", fileName='" + fileName + '\'' +
                ", createdAt=" + createdAt +
                ", size=" + size +
                ", user=" + user +
                ", parentFolder=" + parentFolder +
                '}';
    }
}
