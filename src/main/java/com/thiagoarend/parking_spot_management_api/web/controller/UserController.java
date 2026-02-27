package com.thiagoarend.parking_spot_management_api.web.controller;

import com.thiagoarend.parking_spot_management_api.entity.User;
import com.thiagoarend.parking_spot_management_api.service.UserService;
import com.thiagoarend.parking_spot_management_api.web.dto.UserCreateDto;
import com.thiagoarend.parking_spot_management_api.web.dto.UserPasswordDto;
import com.thiagoarend.parking_spot_management_api.web.dto.UserResponseDto;
import com.thiagoarend.parking_spot_management_api.web.dto.mapper.UserMapper;
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

@RequiredArgsConstructor
@RestController // bean gerenciavel pelo spring
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService; // o atributo final força uma injeção do tipo injeção por construtor

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
        User persistedUser = userService.save(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(persistedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        User persistedUser = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toDto(persistedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UserPasswordDto dto) {
        User persistedUser = userService.updatePassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> persistedUsers = userService.findAll();
        return ResponseEntity.ok(UserMapper.toListDto(persistedUsers));
    }
}
