package com.beyond.bycontract.adapter.controller;

import com.beyond.bycontract.application.service.FileService;
import com.beyond.bycontract.domain.model.File;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name="Files", description = "Gestion des fichiers")
public class FileController {

    @Autowired
    public FileService fileService;

    @Operation(
            summary = "Créer un nouveau fichier",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Fichier créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Erreur dans les données fournies")
            }
    )
    @PostMapping
    public File createFile(File file) {
        return fileService.createFile(file);
    }
}
