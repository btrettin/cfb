package com.example.cfbstats.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameAndTotalStats implements Serializable {
    List<GameStats> gameStats;
    Map<Integer, Stats> totalStats;
}
