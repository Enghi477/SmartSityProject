package ru.top.smartcity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

}
