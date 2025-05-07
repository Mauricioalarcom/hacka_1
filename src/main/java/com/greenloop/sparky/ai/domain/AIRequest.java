package com.greenloop.sparky.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AIRequest {
    private UUID id;
    private LocalDateTime timestamp;
    private String type;  // "chat", "completion", "multimodal"
    private String model;
    private String prompt;
    private String response;
    private String attachmentName;  // para consultas multimodales
}

