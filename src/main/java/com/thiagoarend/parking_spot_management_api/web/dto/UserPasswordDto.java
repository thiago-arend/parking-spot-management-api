package com.thiagoarend.parking_spot_management_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserPasswordDto {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
