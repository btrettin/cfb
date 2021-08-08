package com.example.cfbstats.services;

import com.example.cfbstats.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;

@Service
public class TeamStatsService {

    private final GamesService gamesService;
    private final TeamsService teamsService;

    public TeamStatsService(GamesService gamesService, TeamsService teamsService) {
        this.gamesService = gamesService;
        this.teamsService = teamsService;
    }

    public GameAndTotalStats getStatsForWeek( Map<Integer, Stats> totalStats, List<Game> currentWeekGames, Map<Integer, Betting> betting) {
        List<GameStats> weekGamesStats = new ArrayList<>();
        Map<Integer, Stats> total = new HashMap<>(totalStats);

        for (Game game : currentWeekGames) {
            int homeId = game.getHome_id();
            int awayId = game.getAway_id();

            boolean homeWin = game.getHome_points() > game.getAway_points();
            List<Line> lines = betting.get(game.getId()).getLines();
            BigDecimal bettingLine;
            if(lines.size() < 1){
                bettingLine = BigDecimal.ZERO;
            } else{
                bettingLine = lines.get(0).getSpread();
            }
            weekGamesStats.add(new GameStats(game, homeWin, total.get(homeId), total.get(awayId), bettingLine));

            // list of stats
            Pair<BigDecimal, BigDecimal> elos = calculateElo(game, total, homeWin);

            // set teams stats
            Stats homeStats = getDeepStats(total.get(homeId));
            homeStats.addGame();
            homeStats.setElo(elos.getValue0());
            homeStats.setAveragePointsAllowed(getAveragePointsAllowed(game.getAway_points(), total.get(homeId)));
            homeStats.setAveragePointsFor(getAveragePointsFor(game.getHome_points(), total.get(homeId)));

            Stats awayStats = getDeepStats(total.get(awayId));
            awayStats.addGame();
            awayStats.setElo(elos.getValue1());
            awayStats.setAveragePointsAllowed(getAveragePointsAllowed(game.getHome_points(), total.get(awayId)));
            awayStats.setAveragePointsFor(getAveragePointsFor(game.getAway_points(), total.get(awayId)));

            total.put(homeId, homeStats);
            total.put(awayId, awayStats);
        }

        return new GameAndTotalStats(weekGamesStats, total);
    }

    public List<FlatGameStats> getTeamStats(){
        List<Betting> lines2018list = gamesService.getLines2018();
        Map<Integer, Betting> lines2018 = lines2018list.stream().collect(Collectors.toMap(Betting::getId, betting -> betting));
        List<Betting> lines2019list = gamesService.getLines2019();
        Map<Integer, Betting> lines2019 = lines2019list.stream().collect(Collectors.toMap(Betting::getId, betting -> betting));
        List<Talent> talents2018 = teamsService.getTalent2018();
        List<Talent> talents2019 = teamsService.getTalent2019();
        Map<Integer, Stats> totalStats = teamsService.getFbsTeams().stream().collect(Collectors.toMap(Team::getId, team -> {
            Optional<Talent> optionalTalent = talents2018.stream().filter(tal -> tal.getSchool().equals(team.getSchool())).findFirst();
            BigDecimal talent = BigDecimal.ZERO;
            if(optionalTalent.isPresent()){
                talent = optionalTalent.get().getTalent();
            }
            return new Stats(team.getId(), team.getSchool(), talent);
        }));

        List<Game> games = gamesService.getGames();
        List<Game> games2018 = gamesService.get2018Games();

        boolean hasGames = true;


        Map<Integer, List<GameStats>> statsFor2018 = new HashMap<>();

        for(int i=1; i<16; i++){
            GameAndTotalStats gameAndTotalStats = getStatsForWeek(totalStats, getCurrentWeekGames(games2018, i), lines2018);
            statsFor2018.put(i,gameAndTotalStats.getGameStats());
            totalStats = getDeepCopy(gameAndTotalStats.getTotalStats());
        }

        totalStats.replaceAll((k,team) -> {
            Optional<Talent> optionalTalent = talents2019.stream().filter(tal -> tal.getSchool().equals(team.getSchool())).findFirst();
            BigDecimal talent = BigDecimal.ZERO;
            if(optionalTalent.isPresent()){
                talent = optionalTalent.get().getTalent();
            }
            team.setTalent(talent);
            return team;
        });

        Map<Integer, List<GameStats>> statsFor2019 = new HashMap<>();

        for(int i=1; i<16; i++){
            GameAndTotalStats gameAndTotalStats = getStatsForWeek(totalStats, getCurrentWeekGames(games, i), lines2019);
            statsFor2019.put(i,gameAndTotalStats.getGameStats());
            totalStats = getDeepCopy(gameAndTotalStats.getTotalStats());
        }

        List<GameStats> stats2018 = statsFor2018.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<GameStats> stats2019 = statsFor2019.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        List<GameStats> stats = Stream.concat(stats2018.stream(), stats2019.stream())
                .collect(Collectors.toList());

        List<FlatGameStats>  simple = stats.stream().map(stat -> {
            FlatGameStats flatStats = new FlatGameStats();
            flatStats.setHomename(stat.getGame().getHome_team());
            flatStats.setAwayname(stat.getGame().getAway_team());
            flatStats.setHomewin(stat.isHomewin());
            flatStats.setEloDifference(stat.getHomeStats().getElo().subtract(stat.getAwayStats().getElo()));
            flatStats.setTalentDifference(stat.getHomeStats().getTalent().subtract(stat.getAwayStats().getTalent()));
            BigDecimal spread = stat.getSpread();
            if(spread == null){
                spread = BigDecimal.ZERO;
            }
            flatStats.setSpread(spread.multiply(BigDecimal.valueOf(-1)));
            BigDecimal scoreMarginDifference = calculateScoreMarginDifference(stat);
            flatStats.setScoreMarginDifference(scoreMarginDifference);
            return flatStats;
        }).collect(Collectors.toList());

        return simple.subList(150,simple.size()-1);
    }

    private BigDecimal calculateScoreMarginDifference(GameStats stats){
        BigDecimal homeMargin = stats.getHomeStats().getAveragePointsFor().subtract(stats.getHomeStats().getAveragePointsAllowed());
        BigDecimal awayMargin = stats.getAwayStats().getAveragePointsFor().subtract(stats.getAwayStats().getAveragePointsAllowed());

        return homeMargin.subtract(awayMargin);
    }

    private Map<Integer, Stats> getDeepCopy(Map<Integer,Stats> stats) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(stats), new TypeToken<HashMap<Integer, Stats>>(){}.getType());
    }

    private Stats getDeepStats(Stats stats) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(stats), new TypeToken<Stats>(){}.getType());
    }

    private List<Game> getCurrentWeekGames(List<Game> games, int i) {
        List<Game> currentWeekGames = new ArrayList<>();
        for (Game game : games) {
            if (game.getWeek() == i) {
                currentWeekGames.add(game);
            }
        }
        return currentWeekGames;
    }

    private BigDecimal getAveragePointsAllowed(Integer pointsAllowed, Stats teamStats) {
        return (teamStats.getAveragePointsAllowed().multiply(BigDecimal.valueOf(teamStats.getGamesPlayed())).add(BigDecimal.valueOf(pointsAllowed))).divide(BigDecimal.valueOf(teamStats.getGamesPlayed() + 1), 3, RoundingMode.FLOOR);
    }

    private BigDecimal getAveragePointsFor(Integer pointsFor, Stats teamStats) {
        return (teamStats.getAveragePointsFor().multiply(BigDecimal.valueOf(teamStats.getGamesPlayed())).add(BigDecimal.valueOf(pointsFor))).divide(BigDecimal.valueOf(teamStats.getGamesPlayed() + 1), 3, RoundingMode.FLOOR);
    }

    private Pair<BigDecimal, BigDecimal> calculateElo(Game game, Map<Integer, Stats> teamStats, boolean homeWin) {
        BigDecimal homeElo = teamStats.get(game.getHome_id()).getElo();
        BigDecimal awayElo = teamStats.get(game.getAway_id()).getElo();
        double pow = Math.pow(10d, homeElo.divide(BigDecimal.valueOf(400), 3, RoundingMode.FLOOR).doubleValue());
        BigDecimal homeR = BigDecimal.valueOf(pow);
        BigDecimal awayR = BigDecimal.valueOf(Math.pow(10d, awayElo.divide(BigDecimal.valueOf(400), 3, RoundingMode.FLOOR).doubleValue()));

        BigDecimal divisor = homeR.add(awayR);
        BigDecimal homeE = homeR.divide(divisor, 3, RoundingMode.FLOOR);
        BigDecimal awayE = awayR.divide(divisor, 3, RoundingMode.FLOOR);
        BigDecimal updatedHomeElo;
        BigDecimal updatedAwayElo;
        BigDecimal k = BigDecimal.valueOf(85);
        Integer pointDifferential = abs(game.getHome_points() - game.getAway_points());
        BigDecimal marginOfVictoryMultiplier;
        if (homeWin) {
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

        updatedHomeElo = updatedHomeElo.setScale(2, RoundingMode.HALF_UP);
        updatedAwayElo = updatedAwayElo.setScale(2, RoundingMode.HALF_UP);
        return new Pair<>(updatedHomeElo, updatedAwayElo);
    }

    private BigDecimal calculateMarginOfVictoryFactor(Integer pointDifferential, BigDecimal eloDiff) {
        return BigDecimal.valueOf(Math.log(pointDifferential + 1)).multiply(BigDecimal.valueOf(2.2).divide(eloDiff.multiply(BigDecimal.valueOf(.001)).add(BigDecimal.valueOf(2.2)), 3, RoundingMode.FLOOR));
    }
}



