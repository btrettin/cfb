package com.example.cfbstats.controllers;

import com.example.cfbstats.models.Team;
import com.example.cfbstats.models.TeamRecord;
import com.example.cfbstats.services.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamsController {

    private final TeamsService teamsService;
    private final TempService tempService;

    @Autowired
    public TeamsController(TeamsService teamsService, TempService tempService) {
        this.teamsService = teamsService;
        this.tempService = tempService;
    }

    @GetMapping(value = "/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Team> getTeams() {
        return teamsService.getTeams();
    }

    @GetMapping(value = "/temp", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getTemp() throws Exception {
        return tempService.getTeams();
    }

    @GetMapping(value = "/records", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TeamRecord> getTeamRecords() {
        return teamsService.getRecords();
    }
}
