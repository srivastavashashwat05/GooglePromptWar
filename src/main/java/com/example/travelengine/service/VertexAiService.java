package com.example.travelengine.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VertexAiService {

    private final String projectId = "linen-flux-495706-e5";
    private final String location = "us-central1";
    private final String modelName = "gemini-1.5-flash";

    public String generateItinerary(String destination, int days, String interests) throws IOException {
        System.out.println("Generating itinerary for: " + destination + " (Days: " + days + ", Interests: " + interests + ")");
        
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAi);

            String prompt = String.format(
                "Generate a detailed %d-day travel itinerary for %s with a focus on %s. " +
                "Return the response in a valid JSON format with the following structure: " +
                "{\"destination\": \"%s\", \"days\": %d, \"itinerary\": [{\"day\": 1, \"activities\": [{\"name\": \"Activity Name\", \"description\": \"Detailed description\", \"lat\": 0.0, \"lng\": 0.0}]}]}. " +
                "Include realistic latitude and longitude coordinates for each activity. " +
                "Do not include any markdown formatting or extra text, just the raw JSON.",
                days, destination, interests, destination, days
            );

            try {
                GenerateContentResponse response = model.generateContent(prompt);
                String text = ResponseHandler.getText(response);
                System.out.println("Successfully generated itinerary for " + destination);
                return text;
            } catch (Exception e) {
                System.err.println("ERROR during Vertex AI content generation: " + e.getMessage());
                e.printStackTrace();
                throw new IOException("Failed to generate itinerary: " + e.getMessage(), e);
            }
        }
    }
}
