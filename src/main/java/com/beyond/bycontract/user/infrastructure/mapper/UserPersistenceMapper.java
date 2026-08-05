package com.beyond.bycontract.user.infrastructure.mapper;

import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;

public class UserPersistenceMapper {
	private UserPersistenceMapper() {
		/* This utility class should not be instantiated */
	}


	public static User entityToDomain(UserEntity userEntity) {
		return new User(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getCreatedAt(), userEntity.getModifiedAt());
	}

	public static UserEntity domainToEntity(User user) {
		UserEntity userEntity = new UserEntity();
		if (user.getId() != null) {
			userEntity.setId(user.getId());
		}

		//createdAt and modifiedAt are not added because they are not updatable and not insertable through an entity,so it's useless to add it

		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());

		return userEntity;
	}

}
