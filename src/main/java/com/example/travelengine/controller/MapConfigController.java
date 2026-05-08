package com.example.travelengine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class MapConfigController {

    @GetMapping("/maps-key")
    public Map<String, String> getMapsKey() {
        String apiKey = System.getenv("MAPS_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            return Map.of("key", "");
        }
        return Map.of("key", apiKey);
    }
}
