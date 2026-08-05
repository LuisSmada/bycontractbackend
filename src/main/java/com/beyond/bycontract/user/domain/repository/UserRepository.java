package com.beyond.bycontract.user.domain.repository;

import com.beyond.bycontract.user.domain.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

	User createUser(User user);

	Optional<User> getUserById(UUID id);

	Optional<User> getUserByEmail(String email);

	void deleteUserById(UUID id);

	User updateUser(UUID idUserToUpdate, User user);

	List<User> getUsersByIds(Collection<UUID> ids);
}
