package com.example.cfbstats.controllers;

import com.example.cfbstats.models.*;
import com.example.cfbstats.services.TeamStatsService;
import com.example.cfbstats.services.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TeamsController {

    private final TeamsService teamsService;
    private final TeamStatsService teamStatsService;

    @Autowired
    public TeamsController(TeamsService teamsService, TeamStatsService teamStatsService) {
        this.teamsService = teamsService;
        this.teamStatsService = teamStatsService;
    }

    @GetMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Team> getTeams() {
        return teamsService.getTeams();
    }


    @GetMapping(value = "/records", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TeamRecord> getTeamRecords() {
        return teamsService.getRecords();
    }

    @GetMapping(value = "/teams/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FlatGameStats> getTeamStats() {
        return teamStatsService.getTeamStats();
    }
}
