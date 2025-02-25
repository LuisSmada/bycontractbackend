package com.beyond.bycontract.adapter.infrastructure.mapper;

import com.beyond.bycontract.adapter.infrastructure.entity.UserEntity;
import com.beyond.bycontract.application.dto.user.SaveUserDto;
import com.beyond.bycontract.domain.model.User;

public class UserMapper {

    public static User entityToDomain(UserEntity userEntity) {
        return new User(userEntity.getIdUser(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getPassword());
    }

    public static UserEntity domainToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        if (user.getIdUser() != null) {
            userEntity.setIdUser(user.getIdUser());
        }
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());

        return userEntity;
    }

}
