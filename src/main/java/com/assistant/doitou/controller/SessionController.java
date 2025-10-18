package com.assistant.doitou.controller;


import com.assistant.doitou.dto.ApiResponse;
import com.assistant.doitou.entity.Session;
import com.assistant.doitou.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Session>>> getAllSessions() {
        try {
            List<Session> sessions = sessionRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success(sessions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve sessions: " + e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Session>>> getActiveSessions() {
        try {
            // Sessions active in last 24 hours
            LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
            List<Session> sessions = sessionRepository.findByLastActivityAfter(yesterday);
            return ResponseEntity.ok(ApiResponse.success(sessions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve active sessions: " + e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSessionStats() {
        try {
            long totalSessions = sessionRepository.count();
            LocalDateTime yesterday = LocalDateTime.now().minusHours(24);
            long activeToday = sessionRepository.findByLastActivityAfter(yesterday).size();
            
            LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
            long newThisWeek = sessionRepository.countByFirstSeenAfter(weekAgo);

            Map<String, Object> stats = Map.of(
                "totalSessions", totalSessions,
                "activeLast24Hours", activeToday,
                "newSessionsThisWeek", newThisWeek
            );

            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve stats: " + e.getMessage()));
        }
    }
}