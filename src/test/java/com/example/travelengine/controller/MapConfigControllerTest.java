package com.example.travelengine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MapConfigController.class)
public class MapConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetMapsKey() throws Exception {
        mockMvc.perform(get("/api/config/maps-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").exists());
    }
}
