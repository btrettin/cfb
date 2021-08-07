package com.example.cfbstats.services;

import com.example.cfbstats.URLs;
import com.example.cfbstats.models.Recruiting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class RecruitingService {

    @Autowired
    public RecruitingService() {}

    public List<Recruiting> getRecruiting(){
        List<Recruiting> recruiting = getRecruitingFromAPI();
        return recruiting;
    }

    private List<Recruiting> getRecruitingFromAPI()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.setBearerAuth("R79Lh35u8BLJp+DlEsbZTCXqHazp4MfyvwXergccwThsXxYGkrRFxkPlq39he2nG");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URLs.TEAMS_RECRUITING);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<Recruiting>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {});

        return response.getBody();
    }
}
