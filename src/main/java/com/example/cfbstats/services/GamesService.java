package com.example.cfbstats.services;

import com.example.cfbstats.Repository.CollegeFootballDataAPI;
import com.example.cfbstats.URLs;
import com.example.cfbstats.models.Betting;
import com.example.cfbstats.models.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.example.cfbstats.utils.HttpBuilder.addQueryParam;

@Service
public class GamesService {
    @Value("classpath:lines-2018.json")
    Resource linesFile2018;
    @Value("classpath:lines-2019.json")
    Resource linesFile2019;
    @Value("classpath:games-2018.json")
    Resource gamesFile2018;
    @Value("classpath:games-2019.json")
    Resource resourceFile;
    private final ObjectMapper objectMapper;

    private final CollegeFootballDataAPI collegeFootballDataAPI;
    @Autowired
    public GamesService(ObjectMapper objectMapper, CollegeFootballDataAPI collegeFootballDataAPI) {
        this.objectMapper = objectMapper;
        this.collegeFootballDataAPI = collegeFootballDataAPI;
    }

    public List<Game> getGames(){
        return getGamesFromJson();
    }

    public List<Game> get2018Games(){
        return get2018GamesFromJson();
    }

    public List<Betting> getLines2018(){
        return getLines2018FromJson();
    }
    public List<Betting> getLines2019(){
        return getLines2019FromJson();
    }

    public List<Betting> getLines2018FromJson() {
        try{
            return objectMapper.readValue(linesFile2018.getInputStream(), new TypeReference<>() {});
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    public List<Betting> getLines2019FromJson() {
        try{
            return objectMapper.readValue(linesFile2019.getInputStream(), new TypeReference<>() {});
        } catch(IOException e){
            throw new RuntimeException();
        }
    }
    public List<Game> getGamesFromJson() {
        try{
            return objectMapper.readValue(resourceFile.getInputStream(), new TypeReference<>() {});
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    public List<Game> get2018GamesFromJson() {
        try{
            return objectMapper.readValue(gamesFile2018.getInputStream(), new TypeReference<>() {});
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    private List<Game> getGamesFromAPI() {
        String url = URLs.GAMES;
        url = addQueryParam(url, "year", "2020");
        url = addQueryParam(url, "seasonType", "regular");

        return collegeFootballDataAPI.get(url, new ParameterizedTypeReference<>() {});
    }
}
