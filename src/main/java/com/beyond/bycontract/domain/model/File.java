package com.beyond.bycontract.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFile;

    private String fileName;
    private LocalDateTime createdAt;
    private int size;

    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)
    private User user;


    @ManyToOne
    @JoinColumn(name="id_parent_folder")
    private Folder parentFolder;

    public File() {}

    public File(Long idFile, String fileName, LocalDateTime createdAt, int size, User user, Folder parentFolder) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.createdAt = createdAt;
        this.size = size;
        this.user = user;
        this.parentFolder = parentFolder;
    }

    public Long getIdFile() {
        return idFile;
    }

    public void setIdFile(Long idFile) {
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
}
