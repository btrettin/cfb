package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {
    private Integer venue_id;
    private String name;
    private String city;
    private String state;
    private String zip;
    private String country_code;
    private String timezone;
    private Double latitude;
    private Double longitude;
    private Double elevation;
    private Double capacity;
    private Double year_constructed;
    private Boolean grass;
    private Boolean dome;
}
