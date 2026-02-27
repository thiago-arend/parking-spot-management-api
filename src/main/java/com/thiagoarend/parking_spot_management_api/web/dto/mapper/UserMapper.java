package com.thiagoarend.parking_spot_management_api.web.dto.mapper;

import com.thiagoarend.parking_spot_management_api.entity.User;
import com.thiagoarend.parking_spot_management_api.web.dto.UserCreateDto;
import com.thiagoarend.parking_spot_management_api.web.dto.UserResponseDto;
import java.util.List;

public class UserMapper {
    public static User toUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        return user;
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setUsername(user.getUsername());
        responseDto.setRole(user.getRole().name().substring("ROLE_".length()));
        return responseDto;
    }

    public static List<UserResponseDto> toListDto(List<User> users) {
        return users.stream().map(user -> toDto(user)).toList(); // .collect(Collectors.toList()) se quisesse uma lista mutável
    }
}
