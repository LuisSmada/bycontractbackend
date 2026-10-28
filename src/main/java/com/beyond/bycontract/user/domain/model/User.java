package com.beyond.bycontract.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
	private UUID id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private UserRole role;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public User() {
	}

	//CONSTRUCTOR FOR THE CREATION IN THE CONTROLLER
	public User(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public User(UUID id, String firstName, String lastName, String email, String password, UserRole role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	//THE ID AND ROLE ARE NULL BECAUSE THE GENERATION OF THE ID IS HANDLED BY THE DATABASE
	public static User create(String firstName, String lastName, String email, String password) {
		return new User(null, firstName, lastName, email, password, null, LocalDateTime.now(), LocalDateTime.now());
	}

	public void updateUser(String newFirstName, String newLastName, String newEmail) {
		if(!newFirstName.trim().isEmpty()) {
			this.firstName = newFirstName;
		}
		if(!newLastName.trim().isEmpty()) {
			this.lastName = newLastName;
		}
		if(!newEmail.trim().isEmpty()) {
			this.email = newEmail;
		}
	}

	public void changePassword(String newPassword) {
		if(!newPassword.trim().isEmpty()) {
			this.password = newPassword;
		}
	}

	public UUID getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public UserRole getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
