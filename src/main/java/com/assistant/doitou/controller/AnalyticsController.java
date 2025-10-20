package com.assistant.doitou.controller;

import com.assistant.doitou.dto.ApiResponse;
import com.assistant.doitou.dto.analytics.*;
import com.assistant.doitou.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * GET /api/analytics/overview
     * Returns overall metrics summary
     */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<OverallMetrics>> getOverview() {
        try {
            OverallMetrics metrics = analyticsService.getOverallMetrics();
            return ResponseEntity.ok(ApiResponse.success(metrics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch overview: " + e.getMessage()));
        }
    }

    /**
     * GET /api/analytics/daily-active-users?days=30
     * Returns daily active users for the last N days
     */
    @GetMapping("/daily-active-users")
    public ResponseEntity<ApiResponse<List<DailyMetric>>> getDailyActiveUsers(
            @RequestParam(defaultValue = "30") int days) {
        try {
            if (days < 1 || days > 365) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Days parameter must be between 1 and 365"));
            }
            List<DailyMetric> metrics = analyticsService.getDailyActiveUsers(days);
            return ResponseEntity.ok(ApiResponse.success(metrics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch daily active users: " + e.getMessage()));
        }
    }

    /**
     * GET /api/analytics/top-questions?limit=10
     * Returns top N most frequently asked questions
     */
    @GetMapping("/top-questions")
    public ResponseEntity<ApiResponse<List<QuestionFrequency>>> getTopQuestions(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            if (limit < 1 || limit > 100) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Limit parameter must be between 1 and 100"));
            }
            List<QuestionFrequency> questions = analyticsService.getTopQuestions(limit);
            return ResponseEntity.ok(ApiResponse.success(questions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch top questions: " + e.getMessage()));
        }
    }

    /**
     * GET /api/analytics/conversion-rate
     * Returns email capture conversion rate
     */
    @GetMapping("/conversion-rate")
    public ResponseEntity<ApiResponse<ConversionMetrics>> getConversionRate() {
        try {
            ConversionMetrics metrics = analyticsService.getConversionRate();
            return ResponseEntity.ok(ApiResponse.success(metrics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch conversion rate: " + e.getMessage()));
        }
    }

    /**
     * GET /api/analytics/engagement?days=7
     * Returns engagement metrics for a specific period
     */
    @GetMapping("/engagement")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEngagementMetrics(
            @RequestParam(defaultValue = "7") int days) {
        try {
            if (days < 1 || days > 365) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Days parameter must be between 1 and 365"));
            }
            Map<String, Object> metrics = analyticsService.getEngagementMetrics(days);
            return ResponseEntity.ok(ApiResponse.success(metrics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch engagement metrics: " + e.getMessage()));
        }
    }
}