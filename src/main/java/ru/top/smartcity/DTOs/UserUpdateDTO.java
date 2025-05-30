package ru.top.smartcity.DTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @NotBlank(message = "Имя не должно быть пустым")
    private String firstname;

    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastname;

    @Min(value = 18, message = "Возраст не должен быть меньше 18")
    @Max(value = 100, message = "Возраст не должен быть больше 100")
    private Integer age;

    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Некорректный формат телефона")
    private String phone;

    @Email(message = "Некорректный email")
    private String email;
}
