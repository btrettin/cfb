package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameStats implements Serializable {
    private Game game;
    private boolean homewin;
    private Stats homeStats;
    private Stats awayStats;
    private BigDecimal spread;
}
