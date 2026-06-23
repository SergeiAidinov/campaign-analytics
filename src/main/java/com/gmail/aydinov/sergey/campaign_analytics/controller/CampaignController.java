package com.gmail.aydinov.sergey.campaign_analytics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    @GetMapping
    public List<String> getAll() {
        return List.of("campaign1", "campaign2");
    }
}