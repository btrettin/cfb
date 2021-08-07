package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Team {
    private int id;
    private String school;
    private String mascot;
    private String abbreviation;
    private String alt_name_1;
    private String alt_name_2;
    private String alt_name_3;
    private String conference;
    private String division;
    private String color;
    private String alt_color;
    private List<String> logos;
    private Location location;
}
