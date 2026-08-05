package com.beyond.bycontract.folder.infrastructure.entity;

import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "folders")
public class FolderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private UUID id;

	@Column(name = "name")
	private String name;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	@Column(name = "size_bytes")
	private long size;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_author", nullable = false)
	private UserEntity author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_parent_folder")
	private FolderEntity parentFolder;

	public FolderEntity() {
	}

	public FolderEntity(UUID id, String name, LocalDateTime createdAt, LocalDateTime modifiedAt, long size, UserEntity author, FolderEntity parentFolder) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.size = size;
		this.author = author;
		this.parentFolder = parentFolder;
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

	public void setId(UUID id) {
		this.id = id;
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

	public UserEntity getAuthor() {
		return author;
	}

	public void setAuthor(UserEntity author) {
		this.author = author;
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
				"id=" + id +
				", name='" + name + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				", size=" + size +
				", author=" + author +
				", parentFolder=" + parentFolder +
				'}';
	}
}
