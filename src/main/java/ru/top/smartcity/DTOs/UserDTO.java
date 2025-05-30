package ru.top.smartcity.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private Integer age;
    private String phone;
    private String email;

}
