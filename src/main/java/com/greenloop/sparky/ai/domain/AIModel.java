package com.greenloop.sparky.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AIModel {
    private String id;
    private String description;
    private List<String> capabilities;

}
