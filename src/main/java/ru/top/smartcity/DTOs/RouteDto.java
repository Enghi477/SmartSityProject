package ru.top.smartcity.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
    private List<PlaceDto> waypoints;
    private double distance;
    private double duration;
    private String polyline;

}
