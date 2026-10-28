package com.beyond.bycontract.user.application.service;

import com.beyond.bycontract.shared.utils.AuthorizationUtils;
import com.beyond.bycontract.user.application.dto.ChangeUserPasswordCommand;
import com.beyond.bycontract.user.application.dto.CreateUserCommand;
import com.beyond.bycontract.user.application.dto.UpdateUserCommand;
import com.beyond.bycontract.user.application.dto.UserResponse;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

	private UserResponse buildUserResponse(User user) {
		return new UserResponse(
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getCreatedAt(),
				user.getModifiedAt()
		);
	}

	@Transactional
	public UserResponse createUser(CreateUserCommand command) {
		User user = User.create(
				command.firstName(),
				command.lastName(),
				command.email(),
				command.password()
		);
		User savedUser = userRepository.createUser(user);
		return buildUserResponse(savedUser);
	}

	public UserResponse getUserById(UUID id) {
		User user = userRepository.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
		return buildUserResponse(user);
	}

	@Transactional
	public void deleteUserById(UUID id) {
		userRepository.deleteUserById(id);
	}

	@Transactional
	public UserResponse updateUser(UpdateUserCommand command) {
		User user = userRepository.getUserById(command.idUser()).orElseThrow(() -> new UserNotFoundException(command.idUser()));
		AuthorizationUtils.verifyOwnership(user.getId(), command.idRequester());
		user.updateUser(
				command.firstName(),
				command.lastName(),
				command.email()
		);

		User updatedUser = userRepository.updateUser(user);
		return buildUserResponse(updatedUser);
	}

	public void changePassword(ChangeUserPasswordCommand command) {
		User user = userRepository.getUserById(command.idUser()).orElseThrow(() -> new UserNotFoundException(command.idUser()));
		if(!passwordEncoder.matches(command.oldPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Old password does not match");
		}

		if(passwordEncoder.matches(command.newPassword(), user.getPassword() )) {
			throw new IllegalArgumentException("New password should not be the same as the old password");
		}

		user.changePassword(passwordEncoder.encode(command.newPassword()));
		userRepository.changeUserPassword(user);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}
}
