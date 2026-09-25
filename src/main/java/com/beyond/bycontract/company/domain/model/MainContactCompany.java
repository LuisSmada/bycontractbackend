package com.beyond.bycontract.company.domain.model;

import java.util.UUID;

public class MainContactCompany {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public MainContactCompany() {}

    public MainContactCompany(UUID id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    //CONTROLLER USED IN THE COMPANY SERVICE
    public MainContactCompany( String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public void update(String newFirstName, String newLastName, String newEmail, String newPhone) {
        // On met à jour uniquement si la nouvelle valeur est fournie (non null)
        // ET si elle n'est pas vide (après suppression des espaces).
        // Cela empêche un utilisateur de vider un champ obligatoire.
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
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
