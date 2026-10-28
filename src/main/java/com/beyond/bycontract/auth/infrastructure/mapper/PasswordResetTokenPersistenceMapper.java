package com.beyond.bycontract.auth.infrastructure.mapper;

import com.beyond.bycontract.auth.domain.model.PasswordResetToken;
import com.beyond.bycontract.auth.infrastructure.entity.PasswordResetTokenEntity;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.model.MainContactCompany;
import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.entity.MainContactCompanyEntity;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class PasswordResetTokenPersistenceMapper {

    public static PasswordResetToken reconstituteDomain(PasswordResetTokenEntity tokenEntity) {
        return new PasswordResetToken(
                tokenEntity.getId(),
                tokenEntity.getToken(),
                tokenEntity.getUser().getId(),
                tokenEntity.getExpiryDate()
        );
    }

    public static PasswordResetTokenEntity toEntity(PasswordResetToken tokenDomain) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setToken(tokenDomain.getToken());
        entity.setExpiryDate(tokenDomain.getExpiryDate());

        if(tokenDomain.getIdUser() != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(tokenDomain.getIdUser());
            entity.setUser(userEntity);
        }

        return entity;
    }
}
