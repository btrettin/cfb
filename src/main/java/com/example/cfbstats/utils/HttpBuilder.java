package com.example.cfbstats.utils;

import org.springframework.web.util.UriComponentsBuilder;

public class HttpBuilder {

    public static String addQueryParam(String url, String param, String value){
        return UriComponentsBuilder.fromHttpUrl(url).queryParam(param, value).toUriString();
    }
}
