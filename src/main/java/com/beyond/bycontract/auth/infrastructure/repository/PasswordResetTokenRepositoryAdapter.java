package com.beyond.bycontract.auth.infrastructure.repository;

import com.beyond.bycontract.auth.domain.model.PasswordResetToken;
import com.beyond.bycontract.auth.domain.repository.PasswordResetTokenRepository;
import com.beyond.bycontract.auth.infrastructure.entity.PasswordResetTokenEntity;
import com.beyond.bycontract.auth.infrastructure.mapper.PasswordResetTokenPersistenceMapper;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.infrastructure.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

    private final JpaPasswordResetTokenRepository jpaTokenRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(PasswordResetToken passwordResetToken) {
        User user = jpaUserRepository.getUserById(passwordResetToken.getIdUser()).orElseThrow(() -> new UserNotFoundException(passwordResetToken.getIdUser()));
        PasswordResetTokenEntity entity = PasswordResetTokenPersistenceMapper.toEntity(passwordResetToken);
        jpaTokenRepository.save(entity);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaTokenRepository.findByToken(token).map(PasswordResetTokenPersistenceMapper::reconstituteDomain);
    }

    @Override
    public void deleteByToken(String token) {
        jpaTokenRepository.deleteByToken(token);
    }

    @Override
    public void deleteByIdUser(UUID userId) {
        jpaTokenRepository.deleteByIdUser(userId);
        jpaTokenRepository.flush(); // 🚨 MAGIE : Force Hibernate à envoyer le DELETE à PostgreSQL immédiatement
    }
}
