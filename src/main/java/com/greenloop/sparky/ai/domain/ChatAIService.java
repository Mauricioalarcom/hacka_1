package com.greenloop.sparky.ai.domain;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.*;
import com.azure.core.credential.AzureKeyCredential;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;

@Service
public class ChatAIService {
    private final ChatCompletionsClient client;
    // Obtener historial de solicitudes
    @Getter
    private final List<AIRequest> requestHistory = new ArrayList<>();

    @Getter
    private final List<AIModel> availableModels = Arrays.asList(
        new AIModel("openai/gpt-4.1-nano", "Modelo ligero para tareas generales de texto", Arrays.asList("text")),
        new AIModel("openai/gpt-4o", "Modelo avanzado para tareas complejas de texto", Arrays.asList("text")),
        new AIModel("openai/dall-e-3", "Modelo para generación de imágenes", Arrays.asList("image")),
        new AIModel("anthropic/claude-3-opus", "Modelo multimodal avanzado", Arrays.asList("text", "image", "video"))
    );

    public ChatAIService() {
        String key = "GITHUB_TOKEN_PLACEHOLDER";
        String endpoint = "https://models.github.ai/inference";

        this.client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }

    public String chat(String userPrompt) {
        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage("You are a helpful assistant specialized in energy management and sustainability."),
                new ChatRequestUserMessage(userPrompt)
        );

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        String model = "openai/gpt-4.1-nano";
        options.setModel(model);

        ChatCompletions completions = client.complete(options);
        String response = "";
        
        if (completions.getChoices() != null && !completions.getChoices().isEmpty()) {
            response = completions.getChoices().get(0).getMessage().getContent();
        } else {
            response = "No se recibieron respuestas del modelo.";
        }
        
        // Guardar en historial
        requestHistory.add(new AIRequest(
            UUID.randomUUID(),
            LocalDateTime.now(),
            "chat",
            model,
            userPrompt,
            response,
            null
        ));
        
        return response;
    }
    
    // Método para completion
    public String textCompletion(CompletionRequest request) {
        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage(request.getSystemPrompt()),
                new ChatRequestUserMessage(request.getUserPrompt())
        );

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        options.setModel(request.getModel());
        if (request.getMaxTokens() > 0) {
            options.setMaxTokens(request.getMaxTokens());
        }
        if (request.getTemperature() > 0) {
            options.setTemperature((double) request.getTemperature());
        }

        ChatCompletions completions = client.complete(options);
        String response = "";
        
        if (completions.getChoices() != null && !completions.getChoices().isEmpty()) {
            response = completions.getChoices().get(0).getMessage().getContent();
        } else {
            response = "No se recibieron respuestas del modelo.";
        }
        
        // Guardar en historial
        requestHistory.add(new AIRequest(
            UUID.randomUUID(),
            LocalDateTime.now(),
            "completion",
            request.getModel(),
            request.getUserPrompt(),
            response,
            null
        ));
        
        return response;
    }
    

    public String multimodalQuery(String prompt, MultipartFile imageFile) throws IOException {
        // Convert image to Base64
        String base64Image = convertToBase64(imageFile);

        // Create messages for the request
        List<ChatRequestMessage> messages = new ArrayList<>();

        // Add system message
        messages.add(new ChatRequestSystemMessage("You are a helpful assistant that can analyze images and text."));

        // Create user message with text and image reference
        // Format the content as JSON with text and image URL
        String content = String.format(
                "{ \"type\": \"text\", \"text\": \"%s\" }\n{ \"type\": \"image_url\", \"image_url\": { \"url\": \"%s\" } }",
                prompt.replace("\"", "\\\""),
                base64Image.replace("\"", "\\\"")
        );

        messages.add(new ChatRequestUserMessage(content));

        // Configure the request
        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        String model = "anthropic/claude-3-opus"; // Model supporting images
        options.setModel(model);

        // Make the request
        ChatCompletions completions = client.complete(options);
        String response = "";

        if (completions.getChoices() != null && !completions.getChoices().isEmpty()) {
            response = completions.getChoices().get(0).getMessage().getContent();
        } else {
            response = "No se recibieron respuestas del modelo.";
        }

        // Save to history
        requestHistory.add(new AIRequest(
                UUID.randomUUID(),
                LocalDateTime.now(),
                "multimodal",
                model,
                prompt,
                response,
                imageFile.getOriginalFilename()
        ));

        return response;
    }
    
    // Método para convertir archivos a Base64
    private String convertToBase64(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream());
        
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        
        byte[] fileBytes = outputStream.toByteArray();
        return "data:" + file.getContentType() + ";base64," + Base64.getEncoder().encodeToString(fileBytes);
    }

}