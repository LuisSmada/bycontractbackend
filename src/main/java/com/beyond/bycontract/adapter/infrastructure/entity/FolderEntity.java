package com.beyond.bycontract.adapter.infrastructure.entity;

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

    @Column(name="size")
    private int size;

    @ManyToOne
    @JoinColumn(name="id_user", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_parent_folder")
    private FolderEntity parentFolder;

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

    @Override
    public String toString() {
        return "FolderEntity{" +
                "idFolder='" + idFolder + '\'' +
                ", folderName='" + folderName + '\'' +
                ", createdAt=" + createdAt +
                ", size=" + size +
                ", user=" + user +
                ", parentFolder=" + parentFolder +
                '}';
    }
}
