package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class Stats{
    private int id;
    private String school;
    private BigDecimal elo;

    public Stats(int id, String school, boolean isFbs){
        this.id = id;
        this.school = school;
        if(isFbs){
            this.elo = BigDecimal.valueOf(1500);
        } else {
            this.elo = BigDecimal.valueOf(1200);
        }
    }
}
