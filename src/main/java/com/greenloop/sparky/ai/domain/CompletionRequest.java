package com.greenloop.sparky.ai.domain;

import lombok.Data;

@Data
public class CompletionRequest {
    private String model = "openai/gpt-4.1-nano";
    private String systemPrompt = "You are a helpful assistant specialized in energy management and sustainability.";
    private String userPrompt;
    private int maxTokens = 0;  // 0 significa usar el valor predeterminado
    private float temperature = 0; // 0 significa usar el valor predeterminado
}
