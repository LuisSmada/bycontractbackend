package com.beyond.bycontract.file.adapter.controller;

import com.beyond.bycontract.file.application.dto.SaveFileDto;
import com.beyond.bycontract.file.application.dto.UpdateFileDto;
import com.beyond.bycontract.file.application.service.FileService;
import com.beyond.bycontract.file.domain.model.File;
import com.beyond.bycontract.folder.application.service.FolderService;
import com.beyond.bycontract.user.application.service.UserService;
import com.beyond.bycontract.user.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "Files", description = "File Manager")
public class FileController {

	private final UserService userService;
	private final FileService fileService;
	private final FolderService folderService;

	@Autowired
	public FileController(UserService userService, FileService fileService, FolderService folderService) {
		this.userService = userService;
		this.fileService = fileService;
		this.folderService = folderService;
	}

	@Operation(
			summary = "Create a new file",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "File object that needs to be added to the system",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = SaveFileDto.class)
					)
			),
			responses = {
					@ApiResponse(responseCode = "201", description = "File created"),
					@ApiResponse(responseCode = "400", description = "Invalid input data"),
					@ApiResponse(responseCode = "500", description = "Internal server error"),
			}
	)
	@PostMapping
	public ResponseEntity<File> createFile(@RequestBody SaveFileDto saveFileDto) {

		Optional<User> userFound = userService.getUserById(saveFileDto.getIdAuthor());

		if (userFound.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		User user = userFound.get();

//        Folder parentFolder = null;
//        if(saveFileDto.getIdParentFolder() != null) {
//            Optional<Folder> parentFolderFound = folderService.getFolderById(saveFileDto.getIdParentFolder());
//            if(parentFolderFound.isEmpty()) {
//                return ResponseEntity.badRequest().body(null);
//            }
//            parentFolder = parentFolderFound.get();
//        }

		File file = new File(saveFileDto.getId(), saveFileDto.getName(), saveFileDto.getSize(), user.getId(), saveFileDto.getIdParentFolder());


		return ResponseEntity.ok(fileService.createFile(file));
	}


	@Operation(
			summary = "Upload a file",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "File object that needs to be added to the system",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = SaveFileDto.class)
					)
			),
			responses = {
					@ApiResponse(responseCode = "201", description = "File created"),
					@ApiResponse(responseCode = "400", description = "Invalid input data"),
					@ApiResponse(responseCode = "500", description = "Internal server error"),
			}
	)
	@PostMapping("/upload")
	public ResponseEntity<File> uploadFile(
			@RequestParam("idFile") UUID idFile,
			@RequestParam("idAuthor") UUID idAuthor,
			@RequestParam(value = "idParentFolder", required = false) UUID idParentFolder,
			@RequestParam("file") MultipartFile file
	) throws IOException {
		try {
			return ResponseEntity.ok(fileService.uploadFile(idFile, idAuthor, idParentFolder, file));

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@Operation(
			summary = "Get a file by its id",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "File found",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = File.class)
							)
					),
					@ApiResponse(responseCode = "404", description = "File not found"),
					@ApiResponse(responseCode = "500", description = "Internal server error")
			}
	)
	@GetMapping("{id}")
	public ResponseEntity<File> getFileById(@PathVariable UUID id) {
		return fileService.getFileById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}


	@Operation(
			summary = "Delete a file by id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Folder deleted"),
					@ApiResponse(responseCode = "404", description = "No folder with this id"),
					@ApiResponse(responseCode = "500", description = "Internal server error")
			}
	)
	@Parameter(name = "id", description = "IdD of the folder to be deleted", example = "1")
	@DeleteMapping("{id}")
	public void deleteById(@PathVariable UUID id) throws IOException {
		fileService.deleteFileById(id);
	}

	@Operation(
			summary = "Update an existing file",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "File update object containing the new details",
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = UpdateFileDto.class)
					)
			),
			responses = {
					@ApiResponse(responseCode = "200", description = "File successfully updated"),
					@ApiResponse(responseCode = "400", description = "Invalid input data"),
					@ApiResponse(responseCode = "404", description = "File not found"),
					@ApiResponse(responseCode = "500", description = "Internal server error")
			}
	)
	@Parameter(name = "id", description = "ID of the file to update", required = true)
	@PutMapping("{id}")
	public ResponseEntity<File> updateFile(@PathVariable UUID id, @RequestBody UpdateFileDto updateFileDto) throws IOException {
		Optional<File> fileFound = fileService.getFileById(id);
		if (fileFound.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		File file = fileFound.get();

//        Folder parentFolder = null;
//        if(updateFileDto.getIdParentFolder() != null) {
//            Optional<Folder> parentFolderFound = folderService.getFolderById(updateFileDto.getIdParentFolder());
//            if(parentFolderFound.isEmpty()) {
//                return ResponseEntity.notFound().build();
//            }
//            parentFolder = parentFolderFound.get();
//        }

		file.setName(updateFileDto.getName());
		file.setSize(updateFileDto.getSize());
		file.setIdParentFolder(updateFileDto.getIdParentFolder());

		return ResponseEntity.ok(fileService.updateFile(id, file));
	}
}
