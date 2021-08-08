package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Stats {
    private int id;
    private int gamesPlayed;
    private String school;
    private BigDecimal elo;
    private BigDecimal totalPointsAllowed;
    private BigDecimal averagePointsAllowed;
    private BigDecimal averagePointsFor;
    private BigDecimal talent;

    public Stats(int id, String school, BigDecimal talent){
        this.id = id;
        this.school = school;
        this.elo = BigDecimal.valueOf(1500);
        this.gamesPlayed = 0;
        this.totalPointsAllowed = BigDecimal.ZERO;
        this.averagePointsAllowed = BigDecimal.ZERO;
        this.averagePointsFor = BigDecimal.ZERO;
        this.talent = talent;
    }

    public void addGame(){
        this.gamesPlayed ++;
    }
}
