package com.beyond.bycontract.user.application.service;

import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User createUser(User user) {
		return userRepository.createUser(user);
	}

	public Optional<User> getUserById(UUID id) {
		return userRepository.getUserById(id);
	}

	public void deleteUserById(UUID id) {
		userRepository.deleteUserById(id);
	}

	public User updateUser(UUID idUserToUpdate, User user) {
		return userRepository.updateUser(idUserToUpdate, user);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}
}
