package com.example.cfbstats.controllers;

import com.example.cfbstats.models.Recruiting;
import com.example.cfbstats.services.RecruitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecruitingController {

    private final RecruitingService recruitingService;

    @Autowired
    public RecruitingController(RecruitingService recruitingService) {
        this.recruitingService = recruitingService;
    }

    @GetMapping(value = "/recruiting", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Recruiting> getRecruiting() {
        return recruitingService.getRecruiting();
    }
}
