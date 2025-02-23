package com.beyond.bycontract.adapter.infrastructure.mapper;

import com.beyond.bycontract.adapter.infrastructure.entity.UserEntity;
import com.beyond.bycontract.domain.model.User;

public class UserMapper {

    public static User entityToDomain(UserEntity userEntity) {
        return new User(userEntity.getIdUser(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getPassword());
    }

    public static UserEntity domainToEntity(User user) {
        return new UserEntity(user.getIdUser(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }
}
