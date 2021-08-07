package com.example.cfbstats.services;

import com.example.cfbstats.Repository.CollegeFootballDataAPI;
import com.example.cfbstats.URLs;
import com.example.cfbstats.models.Team;
import com.example.cfbstats.models.TeamRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Service
public class TeamsService {
    @Value("classpath:teams.json")
    Resource teamsFile;

    @Value("classpath:fbs-teams.json")
    Resource fbsTeamsFile;
    private final ObjectMapper objectMapper;
    private final CollegeFootballDataAPI collegeFootballDataAPI;

    @Autowired
    public TeamsService(ObjectMapper objectMapper, CollegeFootballDataAPI collegeFootballDataAPI) {
        this.objectMapper = objectMapper;
        this.collegeFootballDataAPI = collegeFootballDataAPI;
    }

    public List<Team> getTeams(){
        return getTeamsFromJson();
    }

    public List<Team> getFbsTeams(){
        return getTeamsFromJson();
    }

    public List<Team> getTeamsFromJson() {
        try{
            return objectMapper.readValue(teamsFile.getInputStream(), new TypeReference<>() {});
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    public List<Team> getFbsTeamsFromJson() {
        try{
            return objectMapper.readValue(fbsTeamsFile.getInputStream(), new TypeReference<>() {});
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    public List<TeamRecord> getRecords(){
        List<TeamRecord> teams = getTeamRecordsFromAPI();
        return teams;
    }

    private List<TeamRecord> getTeamRecordsFromAPI() {
        final String url = "https://api.collegefootballdata.com/records";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.setBearerAuth("R79Lh35u8BLJp+DlEsbZTCXqHazp4MfyvwXergccwThsXxYGkrRFxkPlq39he2nG");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URLs.RECORDS);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<TeamRecord>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {});

        return response.getBody();
    }
}
