package com.example.cfbstats.controllers;

import com.example.cfbstats.models.Game;
import com.example.cfbstats.services.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GamesController {

    private final GamesService gamesService;

    @Autowired
    public GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping(value = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Game> getGames() {
        return gamesService.getGames();
    }
}
