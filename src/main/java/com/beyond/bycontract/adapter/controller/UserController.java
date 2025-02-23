package com.beyond.bycontract.adapter.controller;

import com.beyond.bycontract.application.service.UserService;
import com.beyond.bycontract.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name="Users", description = "Gestion des utilisateurs")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Créer un nouvel utilisateur",
            description = "Ajouter un utilisateur à la base de données",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Erreur dans les données fournies")
            }
    )
    @PostMapping
    public User createUser(User user) {
        return userService.createUser(user);
    }
}
