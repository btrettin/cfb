package com.example.cfbstats.controllers;

import com.example.cfbstats.models.Stats;
import com.example.cfbstats.services.UnbiasedRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RankingController {

    private final UnbiasedRankingService unbiasedRankingService;

    @Autowired
    public RankingController(UnbiasedRankingService unbiasedRankingService) {
        this.unbiasedRankingService = unbiasedRankingService;
    }

    @GetMapping(value = "/rankings/unbiased", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Stats> getGames() {
        return unbiasedRankingService.getUnbiasedRanking();
    }
}
