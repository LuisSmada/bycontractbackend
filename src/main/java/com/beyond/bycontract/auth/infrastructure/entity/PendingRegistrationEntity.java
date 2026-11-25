package com.beyond.bycontract.auth.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pending_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name",  nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "passwd_hash", nullable = false)
    private String passwordHash;

    @Column(name = "otp_hash",  nullable = false)
    private String otpHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "last_sent_at",  nullable = false)
    private LocalDateTime lastSentAt;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @CreationTimestamp //To tell to hibernate to generate the date automatically
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}