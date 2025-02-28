package com.beyond.bycontract.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Folder {
    private String idFolder;
    private String folderName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int size;
    private User user;
    private Folder parentFolder;

    public Folder() {}
    public Folder(String idFolder, String folderName, LocalDateTime createdAt, LocalDateTime modifiedAt, int size, User user, Folder parentFolder) {
        this.idFolder = idFolder;
        this.folderName = folderName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.size = size;
        this.user = user;
        this.parentFolder = parentFolder;
    }

    public Folder(String idFolder, String folderName, int size, User user, Folder parentFolder) {
        this.idFolder = idFolder;
        this.folderName = folderName;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
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
        return "Folder{" +
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
