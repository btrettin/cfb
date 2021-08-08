package com.example.cfbstats.services;

import com.example.cfbstats.models.Game;
import com.example.cfbstats.models.GameStats;
import com.example.cfbstats.models.Stats;
import com.example.cfbstats.models.Team;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Service
public class UnbiasedRankingService {

    private final GamesService gamesService;
    private final TeamsService teamsService;

    @Autowired
    public UnbiasedRankingService(GamesService gamesService, TeamsService teamsService) {
        this.gamesService = gamesService;
        this.teamsService = teamsService;
    }

    public List<Stats> getUnbiasedRanking() {

        return new ArrayList<>();
    }
}