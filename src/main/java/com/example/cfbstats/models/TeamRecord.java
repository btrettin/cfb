package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamRecord {
    private int year;
    private String team;
    private String conference;
    private String division;
    private Record total;
    private Record conferenceGames;
    private Record homeGames;
    private Record awayGames;
}
