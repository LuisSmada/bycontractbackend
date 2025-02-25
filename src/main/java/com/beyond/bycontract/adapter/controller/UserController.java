package com.beyond.bycontract.adapter.controller;

import com.beyond.bycontract.application.dto.user.SaveUserDto;
import com.beyond.bycontract.application.dto.user.UpdateUserDto;
import com.beyond.bycontract.application.service.FileService;
import com.beyond.bycontract.application.service.FolderService;
import com.beyond.bycontract.application.service.UserService;
import com.beyond.bycontract.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name="Users", description = "User Manager")
public class UserController {


    private final UserService userService;
    private final FileService fileService;
    private final FolderService folderService;

    @Autowired
    public UserController(UserService userService, FileService fileService, FolderService folderService) {
        this.userService = userService;
        this.fileService = fileService;
        this.folderService = folderService;
    }

    @Operation(
            summary = "Create a new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User object that needs to be added to the system",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaveUserDto.class)
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
    public User createUser(@RequestBody SaveUserDto saveUserDto) {
        User user = new User(saveUserDto.getFirstName(), saveUserDto.getLastName(), saveUserDto.getEmail(), saveUserDto.getPassword() );
        return userService.createUser(user);
    }


    @Operation(
            summary="Retrieve a user by id",
            responses = {
                    @ApiResponse(
                            responseCode ="200",
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
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @Operation(
            summary="Delete a user by id",
            responses = {
                    @ApiResponse(responseCode ="200", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "No user with this id"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @Parameter(name = "id", description = "ID of the user to be deleted", required = true, example = "1")
    @DeleteMapping("{id}")
    public void deleteUserbyId(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @Operation(
            summary = "Update an existing user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update object containing the new details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUserDto.class)
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
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
        Optional<User> optionalUser = userService.getUserById(id);

        if(optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setEmail(updateUserDto.getEmail());
        user.setPassword(updateUserDto.getPassword());

        return ResponseEntity.ok(userService.updateUser(id, user));
    }
}
