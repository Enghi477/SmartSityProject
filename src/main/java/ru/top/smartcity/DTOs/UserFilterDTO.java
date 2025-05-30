package ru.top.smartcity.DTOs;


import lombok.Data;

@Data
public class UserFilterDTO {

    private String firstname;
    private String lastname;
    private Integer minAge;
    private Integer maxAge;
    private String phone;
    private String email;
}

