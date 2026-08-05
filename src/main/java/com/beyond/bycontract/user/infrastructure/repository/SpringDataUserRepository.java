package com.beyond.bycontract.user.infrastructure.repository;

import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
	Optional<UserEntity> findByEmail(String email);
}
