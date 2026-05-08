package com.example.travelengine.controller;

import com.example.travelengine.service.VertexAiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TravelPlanController.class)
public class TravelPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VertexAiService vertexAiService;

    @Test
    public void testGetPlan() throws Exception {
        String mockResponse = "{\"destination\": \"Paris\", \"itinerary\": []}";
        Mockito.when(vertexAiService.generateItinerary("Paris", 3, "food"))
               .thenReturn(mockResponse);

        mockMvc.perform(get("/api/plan")
                .param("destination", "Paris")
                .param("days", "3")
                .param("interests", "food"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockResponse));
    }
}
