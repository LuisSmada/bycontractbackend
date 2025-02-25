package com.beyond.bycontract.adapter.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "folder")
public class FolderEntity {
    @Id
    @Column(name = "id_folder")
    private String idFolder;

    @Column(name="folder_name")
    private String folderName;

    @Column(name="created_at", updatable=false, insertable=false)
    private LocalDateTime createdAt;

    @Column(name="modified_at", insertable=false)
    private LocalDateTime modifiedAt;

    @Column(name="size")
    private int size;

    @ManyToOne
    @JoinColumn(name="id_user", nullable = false)
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

    public FolderEntity() {}

    public FolderEntity(String idFolder, String folderName, LocalDateTime createdAt, int size, UserEntity user, FolderEntity parentFolder) {
        this.idFolder = idFolder;
        this.folderName = folderName;
        this.createdAt = createdAt;
        this.size = size;
        this.user = user;
        this.parentFolder = parentFolder;
    }

    public String getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(String idFolder) {
        this.idFolder = idFolder;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
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
        return "FolderEntity{" +
                "idFolder='" + idFolder + '\'' +
                ", folderName='" + folderName + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", size=" + size +
                ", user=" + user +
                ", parentFolder=" + parentFolder +
                '}';
    }
}
