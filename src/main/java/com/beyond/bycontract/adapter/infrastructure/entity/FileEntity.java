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

    @Column(name="modified_at", insertable=false)
    private LocalDateTime modifiedAt;

    @Column(name="size")
    private int size;

    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_parent_folder")
    private FolderEntity parentFolder;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now(); //I added this because when modifiedAt is alone, its created before createdAt which is not normal
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

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

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "idFile='" + idFile + '\'' +
                ", fileName='" + fileName + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", size=" + size +
                ", user=" + user +
                ", parentFolder=" + parentFolder +
                '}';
    }
}
