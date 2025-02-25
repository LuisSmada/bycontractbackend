package com.beyond.bycontract.adapter.infrastructure.repository.user;

import com.beyond.bycontract.adapter.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
}
