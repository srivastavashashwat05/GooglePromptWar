package com.example.travelengine.controller;

import com.example.travelengine.service.VertexAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TravelPlanController {

    @Autowired
    private VertexAiService vertexAiService;

    @GetMapping("/api/plan")
    public String getPlan(
            @RequestParam String destination,
            @RequestParam(defaultValue = "3") int days,
            @RequestParam(defaultValue = "general") String interests) throws IOException {
        return vertexAiService.generateItinerary(destination, days, interests);
    }
}
