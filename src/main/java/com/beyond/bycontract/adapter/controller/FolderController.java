package com.beyond.bycontract.adapter.controller;

import com.beyond.bycontract.application.service.FolderService;
import com.beyond.bycontract.domain.model.Folder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/folders")
@Tag(name="Folders", description = "Gestion des dossiers")
public class FolderController {

    public FolderService folderService;

    @Operation(
            summary = "Créer un nouveau dossier",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dossier créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Erreur dans les données fournies")
            }
    )
    @PostMapping
    public Folder createFolder(Folder folder) {
        return folderService.createFolder(folder);
    }
}
