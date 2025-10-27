package com.assistant.doitou.controller;


import com.assistant.doitou.dto.ApiResponse;
import com.assistant.doitou.dto.ChatRequest;
import com.assistant.doitou.entity.Conversation;
import com.assistant.doitou.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Conversation>> saveMessage(@RequestBody ChatRequest request) {
        try {
            Conversation conversation = chatService.saveConversation(
                request.getUserId(),
                request.getRole(),
                request.getMessage()
            );
            return ResponseEntity.ok(ApiResponse.success("Message saved successfully", conversation));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to save message: " + e.getMessage()));
        }
    }

    @PostMapping("/save-user")
    public ResponseEntity<ApiResponse<Conversation>> saveUserMessage(@RequestBody ChatRequest request) {
        try {
            Conversation conversation = chatService.saveConversation(
                request.getUserId(),
                "user",
                request.getMessage()
            );
            return ResponseEntity.ok(ApiResponse.success("Message saved successfully", conversation));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to save message: " + e.getMessage()));
        }
    }

    @PostMapping("/save-assistant")
    public ResponseEntity<ApiResponse<Conversation>> saveAssistantMessage(@RequestBody ChatRequest request) {
        try {
            Conversation conversation = chatService.saveConversation(
                request.getUserId(),
                "assistant",
                request.getMessage()
            );
            return ResponseEntity.ok(ApiResponse.success("Message saved successfully", conversation));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to save message: " + e.getMessage()));
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<List<Conversation>>> getHistory(@PathVariable String userId) {
        try {
            List<Conversation> history = chatService.getConversationHistory(userId);
            return ResponseEntity.ok(ApiResponse.success(history));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve history: " + e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<Conversation>>> getRecentConversations() {
        try {
            List<Conversation> conversations = chatService.getRecentConversations(100);
            return ResponseEntity.ok(ApiResponse.success(conversations));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve conversations: " + e.getMessage()));
        }
    }
}