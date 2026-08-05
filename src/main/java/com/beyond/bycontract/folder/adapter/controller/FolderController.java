package com.beyond.bycontract.folder.adapter.controller;

import com.beyond.bycontract.file.application.service.FileService;
import com.beyond.bycontract.folder.application.dto.SaveFolderDto;
import com.beyond.bycontract.folder.application.dto.UpdateFolderDto;
import com.beyond.bycontract.folder.application.service.FolderService;
import com.beyond.bycontract.folder.domain.exception.FolderNotFoundException;
import com.beyond.bycontract.folder.domain.model.Folder;
import com.beyond.bycontract.user.application.service.UserService;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/folders")
@Tag(name = "Folders", description = "Folder Manager")
public class FolderController {

	private final UserService userService;
	private final FileService fileService;
	private final FolderService folderService;

	@Autowired
	public FolderController(UserService userService, FileService fileService, FolderService folderService) {
		this.userService = userService;
		this.fileService = fileService;
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
	public ResponseEntity<Folder> createFolder(@RequestBody SaveFolderDto saveFolderDto) throws IOException {
		Optional<User> userFound = userService.getUserById(saveFolderDto.getIdAuthor());
		if (userFound.isEmpty()) {
			throw new UserNotFoundException("User not found");
		}
		User user = userFound.get();

//        Folder parentFolder = null;
//        if(saveFolderDto.getIdParentFolder() != null) {
//            Optional<Folder> parentFolderFound = folderService.getFolderById(saveFolderDto.getIdParentFolder());
//            if(parentFolderFound.isEmpty()) {
//                throw new FolderNotFoundException("Parent folder not found");
//            }
//            parentFolder = parentFolderFound.get();
//        }

		Folder folder = new Folder(saveFolderDto.getId(), saveFolderDto.getName(), saveFolderDto.getSize(), user.getId(), saveFolderDto.getIdParentFolder());

		return ResponseEntity.ok(folderService.createFolder(folder));
	}


	@Operation(
			summary = "Get a folder by id",
			responses = {
					@ApiResponse(
							responseCode = "200",
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
	public ResponseEntity<Folder> getFolderById(@PathVariable UUID id) {
		Optional<Folder> folderFound = folderService.getFolderById(id);
		if (folderFound.isEmpty()) {
			throw new FolderNotFoundException("Folder not found");
		}
		return ResponseEntity.ok(folderFound.get());
	}


	@Operation(
			summary = "Delete a folder by id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Folder deleted"),
					@ApiResponse(responseCode = "404", description = "No folder with this id"),
					@ApiResponse(responseCode = "500", description = "Internal server error")
			}
	)
	@Parameter(name = "id", description = "ID of the folder to be deleted", required = true, example = "1")
	@DeleteMapping("{id}")
	public void deleteFolderById(@PathVariable UUID id) throws IOException {
		Optional<Folder> folderFound = folderService.getFolderById(id);
		if (folderFound.isEmpty()) {
			throw new FolderNotFoundException("Folder not found");
		}
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
	public ResponseEntity<Folder> updateFolderById(@PathVariable UUID id, @RequestBody UpdateFolderDto updateFolderDto) throws IOException {

		Optional<Folder> folderFound = folderService.getFolderById(id);

		if (folderFound.isEmpty()) {
			throw new FolderNotFoundException("Folder not found");
		}

		Folder folder = folderFound.get();

//        Folder parentFolder = null;

//        if(updateFolderDto.getIdParentFolder() != null) {
//            Optional<Folder> parentFolderFound = folderService.getFolderById(updateFolderDto.getIdParentFolder());
//            if(parentFolderFound.isEmpty()) {
//                throw new FolderNotFoundException("Parent folder not found");
//            }
//            parentFolder = parentFolderFound.get();
//        }

		folder.setName(updateFolderDto.getName());
		folder.setSize(updateFolderDto.getSize());
		folder.setIdParentFolder(updateFolderDto.getIdParentFolder());

		return ResponseEntity.ok(folderService.updateFolder(id, folder));
	}
}
