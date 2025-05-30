package ru.top.smartcity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}

