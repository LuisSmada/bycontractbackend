package com.beyond.bycontract.application.service;

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
		User userToCreate = new User();
		userToCreate.setFirstName("Adams");
		userToCreate.setLastName("AYO");
		userToCreate.setEmail("ayoadams@beyond.com");
		userToCreate.setPassword("mdp");

		User savedUser = userService.createUser(userToCreate);

		assertNotNull(savedUser, "user saved should be not null");
		assertNotNull(savedUser.getId(), "The id should be not null");

		assertEquals("Adams", savedUser.getFirstName());
		assertEquals("ayoadams@beyond.com", savedUser.getEmail());
	}
}
