package com.beyond.bycontract.user.presentation;


import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.user.application.dto.UserResponse;
import com.beyond.bycontract.user.application.service.UserService;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.presentation.dto.ChangeUserPasswordRequest;
import com.beyond.bycontract.user.presentation.dto.CreateUserRequest;
import com.beyond.bycontract.user.presentation.dto.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User Manager")
public class UserController {


	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(
			summary = "Create a new user",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "User object that needs to be added to the system",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = CreateUserRequest.class)
					)
			),
			responses = {
					@ApiResponse(responseCode = "200", description = "User created"),
					@ApiResponse(responseCode = "400", description = "Invalid input data"),
					@ApiResponse(responseCode = "409", description = "User already exists"),
					@ApiResponse(responseCode = "500", description = "Internal server error"),
			}
	)
	@Parameter(name = "saveUserDto", description = "User information to be added", required = true)
	@PostMapping
	public UserResponse createUser(@RequestBody CreateUserRequest request) {
		return userService.createUser(request.toCommand());
	}

	@Operation(
			summary = "Retrieve a user by id",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "User found",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = User.class))),
					@ApiResponse(responseCode = "404", description = "User not found"),
					@ApiResponse(responseCode = "500", description = "Internal server error")
			}
	)
	@Parameter(name = "id", description = "ID of the user to find", required = true, example = "1")
	@GetMapping("{id}")
	public UserResponse getUserById(@PathVariable UUID id) {
		return userService.getUserById(id);
	}


	@Operation(
			summary = "Delete a user by id",
			responses = {
					@ApiResponse(responseCode = "200", description = "User deleted"),
					@ApiResponse(responseCode = "404", description = "No user with this id"),
					@ApiResponse(responseCode = "500", description = "Internal server error")
			}
	)
	@Parameter(name = "id", description = "ID of the user to be deleted", required = true, example = "1")
	@DeleteMapping("{id}")
	public void deleteUserbyId(@PathVariable UUID id) {
		userService.deleteUserById(id);
	}

	@Operation(
			summary = "Update an existing user",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "User update object containing the new details",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = UpdateUserRequest.class)
					)
			),
			responses = {
					@ApiResponse(responseCode = "200", description = "User successfully updated"),
					@ApiResponse(responseCode = "400", description = "Invalid input data"),
					@ApiResponse(responseCode = "404", description = "User not found"),
					@ApiResponse(responseCode = "500", description = "Internal server error")
			}
	)
	@Parameter(name = "id", description = "ID of the user to update", required = true)
	@PatchMapping("{id}")
	public UserResponse updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request, @AuthenticationPrincipal CustomUserDetails currentUser ) {
		return userService.updateUser(request.toCommand(id, currentUser.getId()));
	}

	@PatchMapping("/change-password")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Renvoie un code 204 (Succès sans corps)
	public void changePassword(
			@Valid @RequestBody ChangeUserPasswordRequest request,
			@AuthenticationPrincipal CustomUserDetails currentUser
	) {
		userService.changePassword(request.toCommand(currentUser.getId()));
	}
}
