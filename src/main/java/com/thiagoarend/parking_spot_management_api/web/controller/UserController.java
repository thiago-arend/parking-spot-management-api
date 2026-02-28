package com.thiagoarend.parking_spot_management_api.web.controller;

import com.thiagoarend.parking_spot_management_api.entity.User;
import com.thiagoarend.parking_spot_management_api.service.UserService;
import com.thiagoarend.parking_spot_management_api.web.dto.UserCreateDto;
import com.thiagoarend.parking_spot_management_api.web.dto.UserPasswordDto;
import com.thiagoarend.parking_spot_management_api.web.dto.UserResponseDto;
import com.thiagoarend.parking_spot_management_api.web.dto.mapper.UserMapper;
import com.thiagoarend.parking_spot_management_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

// '@Tag' identifica o recurso no swagger de maneira geral
@Tag(name = "Users", description = "Holds operations for registering, editing and reading user's information.")
@RequiredArgsConstructor
@RestController // bean gerenciavel pelo spring
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService; // o atributo final força uma injeção do tipo injeção por construtor

    @Operation(
            summary = "Create a new user",
            description = "Resource for creating a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Resource created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Resource not processed due to duplicated e-mail",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Resource not processed due to invalid input field values",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto dto) {
        User persistedUser = userService.save(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(persistedUser));
    }

    @Operation(
            summary = "Retrieve a user by resource id",
            description = "Resource for retrieving a user by its id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resource retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resource not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        User persistedUser = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toDto(persistedUser));
    }

    @Operation(
            summary = "Update a user password",
            description = "Resource for updating the password of a user",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Resource updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Void.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resource not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Password does not match",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Resource not processed due to invalid input field values",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDto dto) {
        User persistedUser = userService.updatePassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Retrieve all users",
            description = "Resource for retrieving all users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resource retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserResponseDto.class))))
            })
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> persistedUsers = userService.findAll();
        return ResponseEntity.ok(UserMapper.toListDto(persistedUsers));
    }
}
