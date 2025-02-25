package com.beyond.bycontract.adapter.controller;

import com.beyond.bycontract.application.dto.folder.SaveFolderDto;
import com.beyond.bycontract.application.dto.folder.UpdateFolderDto;
import com.beyond.bycontract.application.service.FileService;
import com.beyond.bycontract.application.service.FolderService;
import com.beyond.bycontract.application.service.UserService;
import com.beyond.bycontract.domain.model.Folder;
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
@RequestMapping("/api/v1/folders")
@Tag(name="Folders", description = "Folder Manager")
public class FolderController {

    private final UserService userService;
    private final FileService fileService;
    private final FolderService folderService;

    @Autowired
    public FolderController(UserService userService, FileService fileService, FolderService folderService) {
        this.userService = userService;
        this.fileService = fileService ;
        this.folderService = folderService;
    }

    @Operation(
            summary = "Create a new folder",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Folder object that needs to be added to the system",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaveFolderDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Folder created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<Folder> createFolder(@RequestBody SaveFolderDto saveFolderDto) {
        Optional<User> userFound = userService.getUserById(saveFolderDto.getIdUser());
        if(userFound.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userFound.get();

        Folder parentFolder = null;
        if(saveFolderDto.getIdParentFolder() != null) {
            Optional<Folder> parentFolderFound = folderService.getFolderById(saveFolderDto.getIdParentFolder());
            if(parentFolderFound.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            parentFolder = parentFolderFound.get();
        }

        Folder folder = new Folder(saveFolderDto.getIdFolder(), saveFolderDto.getFolderName(), saveFolderDto.getSize(), user, parentFolder);

        return ResponseEntity.ok(folderService.createFolder(folder));
    }


    @Operation(
            summary="Get a folder by id",
            responses = {
                    @ApiResponse(
                            responseCode ="200",
                            description = "Folder found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Folder.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Folder not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<Folder> getFolderById(@PathVariable String id) {
        return folderService.getFolderById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



    @Operation(
            summary="Delete a folder by id",
            responses = {
                    @ApiResponse(responseCode ="200", description = "Folder deleted"),
                    @ApiResponse(responseCode = "404", description = "No folder with this id"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @Parameter(name = "id", description = "ID of the folder to be deleted", required = true, example = "1")
    @DeleteMapping("{id}")
    public void deleteFolderById(@PathVariable String id) {
        folderService.deleteFolderById(id);
    }



    @Operation(
            summary = "Update an existing folder",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Folder update object containing the new details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateFolderDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Folder successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Folder not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @Parameter(name = "id", description = "ID of the folder to update", required = true)
    @PutMapping("{id}")
    public ResponseEntity<Folder> updateFolderById(@PathVariable String id, @RequestBody UpdateFolderDto updateFolderDto) {

        Optional<Folder> folderFound = folderService.getFolderById(id);

        if (folderFound.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Folder folder = folderFound.get();

        Folder parentFolder = null;

        if(updateFolderDto.getIdParentFolder() != null) {
            Optional<Folder> parentFolderFound = folderService.getFolderById(updateFolderDto.getIdParentFolder());
            if(parentFolderFound.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            parentFolder = parentFolderFound.get();
        }

        folder.setFolderName(updateFolderDto.getFolderName());
        folder.setSize(updateFolderDto.getSize());
        folder.setParentFolder(parentFolder);

        return ResponseEntity.ok(folderService.updateFolder(id, folder));
    }
}
