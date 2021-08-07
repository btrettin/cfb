package com.example.cfbstats.Repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CollegeFootballDataAPI {

    public <T> List<T> get(String url, ParameterizedTypeReference<List<T>> responseType) {
        HttpHeaders headers = buildHeaders();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType).getBody();
    }

    public <T> T get(String url, Class<T> responseType) {
        HttpHeaders headers = buildHeaders();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType).getBody();
    }

    public HttpHeaders buildHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.setBearerAuth("R79Lh35u8BLJp+DlEsbZTCXqHazp4MfyvwXergccwThsXxYGkrRFxkPlq39he2nG");
        return headers;
    }
}
