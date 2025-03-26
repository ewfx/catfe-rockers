package org.rockers.paymentgen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenAIService {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    @Value("${openai.api.key}")
    private String openaiApiKey;
    
    @Value("${openai.api.url}")
    private String openaiUrl;
    
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> generateTestCases(String paymentType, int count, String requirements) throws IOException {
        String prompt = buildPrompt(paymentType, count, requirements);
        String response = callOpenAI(prompt);
        return parseResponse(response);
    }

    private String buildPrompt(String paymentType, int count, String requirements) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate ").append(count).append(" diverse test cases for a ").append(paymentType)
              .append(" payment system. The test cases should cover positive, negative, and edge case scenarios.");
        
        if (requirements != null && !requirements.isEmpty()) {
            prompt.append(" Additional requirements: ").append(requirements);
        }
        
        prompt.append("\n\nFormat each test case as a single line with the following structure:");
        prompt.append("\nTest Case ID: [unique identifier], Description: [brief description], Steps: [key steps], Expected Result: [expected outcome]");
        prompt.append("\n\nGenerated Test Cases:");
        
        return prompt.toString();
    }

    private String callOpenAI(String prompt) throws IOException {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", "gpt-3.5-turbo");
        
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        
        requestMap.put("messages", messages);
        requestMap.put("temperature", 0.7);
        requestMap.put("max_tokens", 2000);
        
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestMap);
        } catch (JsonProcessingException e) {
            throw new IOException("Error creating request body", e);
        }
        
        Request request = new Request.Builder()
                .url(openaiUrl)
                .addHeader("Authorization", "Bearer " + openaiApiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, JSON))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + ": " + response.body().string());
            }
            return response.body().string();
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> parseResponse(String response) throws IOException {
        List<String> testCases = new ArrayList<>();
        
        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IOException("No choices in OpenAI response");
        }
        
        String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
        String[] lines = content.split("\n");
        
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("Generated Test Cases:")) {
                continue;
            }
            testCases.add(line.trim());
        }
        
        return testCases;
    }
}