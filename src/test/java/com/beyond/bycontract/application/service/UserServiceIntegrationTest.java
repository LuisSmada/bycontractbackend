package com.beyond.bycontract.application.service;

import com.beyond.bycontract.user.application.dto.CreateUserCommand;
import com.beyond.bycontract.user.application.dto.UserResponse;
import com.beyond.bycontract.user.application.service.UserService;
import com.beyond.bycontract.user.domain.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	@Test
	@DisplayName("Should create an user and assign an UUID")
	void shouldCreateUserSuccessfully() {
		CreateUserCommand command = new CreateUserCommand("Adams", "AYO", "ayoadams@beyond.com", "mdp");
		UserResponse savedUser = userService.createUser(command);
		assertNotNull(savedUser, "user saved should be not null");
		assertEquals("Adams", savedUser.firstName());
		assertEquals("ayoadams@beyond.com", savedUser.email());
	}
}
