package ru.top.smartcity.DTOs;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstname;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastname;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @NotBlank(message = "Телефон не должен быть пустым")
    @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Некорректный номер телефона")
    private String phone;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 18, message = "Возраст не должен быть меньше 18")
    @Max(value = 100, message = "Возраст не должен быть больше 100")
    private Integer age;
}