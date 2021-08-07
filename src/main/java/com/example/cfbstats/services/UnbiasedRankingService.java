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
        List<Game> games = gamesService.getGames();
        List<Integer> fbsTeamIds = teamsService.getFbsTeams().stream().map(Team::getId).collect(Collectors.toList());
        ;

        Map<Integer, Stats> totalStats = new HashMap<>();
        boolean hasGames = true;
        int currentWeek = 1;
        List<GameStats> gameStats = new ArrayList<>();

        while (hasGames) {
            List<Game> currentWeekGames = new ArrayList<>();
            for (Game game1 : games) {
                if (game1.getWeek() == currentWeek) {
                    currentWeekGames.add(game1);
                }
            }

            if (currentWeekGames.size() == 0) {
                hasGames = false;
            }
            for (Game game : currentWeekGames) {
                int homeId = game.getHome_id();
                int awayId = game.getAway_id();

                if (!totalStats.containsKey(homeId)) {
                    boolean isFbs = fbsTeamIds.contains(homeId);
                    totalStats.put(homeId, new Stats(homeId, game.getHome_team(), isFbs));
                }
                if (!totalStats.containsKey(awayId)) {
                    boolean isFbs = fbsTeamIds.contains(awayId);
                    totalStats.put(awayId, new Stats(awayId, game.getAway_team(), isFbs));
                }

                Pair<BigDecimal, BigDecimal> updatedElos = calculateElo(game, totalStats);
                BigDecimal homeElo = totalStats.get(game.getHome_id()).getElo();
                BigDecimal awayElo = totalStats.get(game.getAway_id()).getElo();
                BigDecimal homeDrop = homeElo.subtract(updatedElos.getValue0());
                BigDecimal awayDrop = awayElo.subtract(updatedElos.getValue1());
                if(homeDrop.subtract(awayDrop).compareTo(BigDecimal.ZERO) > 0){
                    gameStats.add(new GameStats(game, homeDrop));
                } else {
                    gameStats.add(new GameStats(game, awayDrop));
                }
                totalStats.get(homeId).setElo(updatedElos.getValue0());
                totalStats.get(awayId).setElo(updatedElos.getValue1());
            };

            currentWeek++;
        }

        gameStats.sort(Comparator.comparing(GameStats::getEloDrop));
        Collections.reverse(gameStats);
        return new ArrayList<>(totalStats.values());
    }


    private Pair<BigDecimal, BigDecimal> calculateElo(Game game, Map<Integer, Stats> stats) {
        BigDecimal homeElo = stats.get(game.getHome_id()).getElo();
        BigDecimal awayElo = stats.get(game.getAway_id()).getElo();
        BigDecimal homeR = BigDecimal.valueOf(Math.pow(10d, homeElo.divide(BigDecimal.valueOf(400), 8, RoundingMode.CEILING).doubleValue()));
        BigDecimal awayR = BigDecimal.valueOf(Math.pow(10d, awayElo.divide(BigDecimal.valueOf(400), 8, RoundingMode.CEILING).doubleValue()));

        BigDecimal divisor = homeR.add(awayR);
        BigDecimal homeE = homeR.divide(divisor, 8, RoundingMode.CEILING);
        BigDecimal awayE = awayR.divide(divisor, 8, RoundingMode.CEILING);
        BigDecimal updatedHomeElo;
        BigDecimal updatedAwayElo;
        BigDecimal k = BigDecimal.valueOf(85);
        Integer pointDifferential = abs(game.getHome_points() - game.getAway_points());
        BigDecimal marginOfVictoryMultiplier;
        if (game.getHome_points() > game.getAway_points()) {
            marginOfVictoryMultiplier = calculateMarginOfVictoryFactor(pointDifferential, homeElo.subtract(awayElo));
            k = k.multiply(marginOfVictoryMultiplier);
            BigDecimal posDiffHome = BigDecimal.ONE.subtract(homeE);
            BigDecimal negDiffAway = BigDecimal.ZERO.subtract(awayE);
            updatedHomeElo = homeElo.add(k.multiply(posDiffHome));
            updatedAwayElo = awayElo.add(k.multiply(negDiffAway));
        } else {
            marginOfVictoryMultiplier = calculateMarginOfVictoryFactor(pointDifferential, awayElo.subtract(homeElo));
            k = k.multiply(marginOfVictoryMultiplier);
            BigDecimal negDiffHome = BigDecimal.ZERO.subtract(homeE);
            BigDecimal posDiffAway = BigDecimal.ONE.subtract(awayE);
            updatedHomeElo = homeElo.add(k.multiply(negDiffHome));
            updatedAwayElo = awayElo.add(k.multiply(posDiffAway));
        }

        return new Pair<>(updatedHomeElo, updatedAwayElo);
    }


    private BigDecimal calculateMarginOfVictoryFactor(Integer pointDifferential, BigDecimal eloDiff) {
        return BigDecimal.valueOf(Math.log(pointDifferential + 1)).multiply(BigDecimal.valueOf(2.2).divide(eloDiff.multiply(BigDecimal.valueOf(.001)).add(BigDecimal.valueOf(2.2)), 8, RoundingMode.CEILING));
    }
}