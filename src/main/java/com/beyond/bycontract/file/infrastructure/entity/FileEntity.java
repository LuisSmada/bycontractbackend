package com.beyond.bycontract.file.infrastructure.entity;

import com.beyond.bycontract.folder.infrastructure.entity.FolderEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "files")
public class FileEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private UUID id;

	@Column(name = "name")
	private String name;

	@CreationTimestamp //To tell to hibernate to generate the date automatically
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	@Column(name = "size_bytes")
	private long size;

	//LAZY to handle the performance
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_author", nullable = false)
	private UserEntity user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_parent_folder")
	private FolderEntity parentFolder;

	@Column(name = "file_path")
	private String filePath;

	public FileEntity() {
	}

	public FileEntity(UUID id, String name, LocalDateTime createdAt, LocalDateTime modifiedAt, long size, UserEntity user, FolderEntity parentFolder, String filePath) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.size = size;
		this.user = user;
		this.parentFolder = parentFolder;
		this.filePath = filePath;
	}

	@PrePersist
	public void onPrePersist() {
		this.createdAt = LocalDateTime.now(); //I added this because when modifiedAt is alone, its created before createdAt which is not normal
		this.modifiedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onPreUpdate() {
		this.modifiedAt = LocalDateTime.now();
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "FileEntity{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				", size=" + size +
				", user=" + user +
				", parentFolder=" + parentFolder +
				", filePath='" + filePath + '\'' +
				'}';
	}
}
