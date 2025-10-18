package com.assistant.doitou.controller;

import com.assistant.doitou.dto.ApiResponse;
import com.assistant.doitou.dto.QuestionRequest;
import com.assistant.doitou.entity.UnknownQuestion;
import com.assistant.doitou.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/unknown")
    public ResponseEntity<ApiResponse<UnknownQuestion>> saveUnknownQuestion(@RequestBody QuestionRequest request) {
        try {
            UnknownQuestion question = chatService.saveUnknownQuestion(request.getQuestion());
            return ResponseEntity.ok(ApiResponse.success("Question saved successfully", question));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to save question: " + e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<UnknownQuestion>>> getRecentQuestions() {
        try {
            List<UnknownQuestion> questions = chatService.getRecentUnknownQuestions();
            return ResponseEntity.ok(ApiResponse.success(questions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve questions: " + e.getMessage()));
        }
    }
}