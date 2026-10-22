package com.beyond.bycontract.company.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ParticularStakeholder {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ParticularStakeholder() {
    }

    public ParticularStakeholder(UUID id, String firstName, String lastName, String email, String phone, String address, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    private static void validateRequiredFields(String firstName, String lastName, String email) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse email est obligatoire");
        }
    }

    public static ParticularStakeholder create(String firstName, String lastName, String email, String phone, String address) {
        validateRequiredFields(firstName, lastName, email);

        // L'ID et les dates sont à null car ils seront gérés par l'infrastructure (JPA)
        return new ParticularStakeholder(null, firstName, lastName, email, phone, address, null, null);
    }

    public void update(String newFirstName, String newLastName, String newEmail, String newPhone, String newAddress) {
        if (newFirstName != null && !newFirstName.trim().isEmpty()) {
            this.firstName = newFirstName;
        }
        if (newLastName != null && !newLastName.trim().isEmpty()) {
            this.lastName = newLastName;
        }
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            this.email = newEmail;
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            this.phone = newPhone;
        }
        if (newAddress != null && !newAddress.trim().isEmpty()) {
            this.address = newAddress;
        }

        this.modifiedAt = LocalDateTime.now();
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

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
