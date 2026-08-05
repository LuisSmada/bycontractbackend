package com.beyond.bycontract.user.infrastructure.repository;

import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.beyond.bycontract.user.infrastructure.mapper.UserPersistenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserRepository implements UserRepository {

	@Autowired
	private SpringDataUserRepository springDataUserRepository;

	public User createUser(User user) {
		UserEntity userEntity = UserPersistenceMapper.domainToEntity(user);
		return UserPersistenceMapper.entityToDomain(springDataUserRepository.save(userEntity));
	}

	@Override
	public Optional<User> getUserById(UUID id) {
		return springDataUserRepository.findById(id).map(UserPersistenceMapper::entityToDomain);
	}

	@Override
	public void deleteUserById(UUID id) {
		springDataUserRepository.deleteById(id);
	}

	@Override
	public User updateUser(UUID idUserToUpdate, User user) {
		User userFound = springDataUserRepository.findById(idUserToUpdate).map(UserPersistenceMapper::entityToDomain).orElseThrow(() -> new RuntimeException("User not found"));

		if (user.getFirstName() != null) {
			userFound.setFirstName(user.getFirstName());
		}
		if (user.getLastName() != null) {
			userFound.setLastName(user.getLastName());
		}
		if (user.getEmail() != null) {
			userFound.setEmail(user.getEmail());
		}
		if (user.getPassword() != null) {
			userFound.setPassword(user.getPassword());
		}

		UserEntity userEntity = UserPersistenceMapper.domainToEntity(userFound);

		return UserPersistenceMapper.entityToDomain(springDataUserRepository.save(userEntity));
	}

	@Override
	public List<User> getUsersByIds(Collection<UUID> ids) {
		return springDataUserRepository.findAllById(ids).stream().map(UserPersistenceMapper::entityToDomain).toList();
	}

	public Optional<User> getUserByEmail(String email) {
		return springDataUserRepository.findByEmail(email).map(UserPersistenceMapper::entityToDomain);
	}
}
