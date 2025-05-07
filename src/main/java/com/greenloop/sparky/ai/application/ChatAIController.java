package com.greenloop.sparky.ai.application;

import com.greenloop.sparky.ai.domain.AIModel;
import com.greenloop.sparky.ai.domain.AIRequest;
import com.greenloop.sparky.ai.domain.ChatAIService;
import com.greenloop.sparky.ai.domain.CompletionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class ChatAIController {

    private final ChatAIService chatAIService;

    @Autowired
    public ChatAIController(ChatAIService chatAIService) {
        this.chatAIService = chatAIService;
    }

    @PostMapping("/chat")
    public String prompt(@RequestBody String prompt) {
        return chatAIService.chat(prompt);
    }
    
    @PostMapping("/completion")
    public String completion(@RequestBody CompletionRequest request) {
        return chatAIService.textCompletion(request);
    }
    
    @PostMapping(value = "/multimodal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String multimodal(
            @RequestParam("prompt") String prompt,
            @RequestParam("image") MultipartFile imageFile) throws IOException {
        return chatAIService.multimodalQuery(prompt, imageFile);
    }
    
    @GetMapping("/models")
    public List<AIModel> getAvailableModels() {
        return chatAIService.getAvailableModels();
    }
    
    @GetMapping("/history")
    public List<AIRequest> getRequestHistory() {
        return chatAIService.getRequestHistory();
    }
}