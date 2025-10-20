package com.assistant.doitou.service;

import com.assistant.doitou.dto.analytics.*;
import com.assistant.doitou.entity.Conversation;
import com.assistant.doitou.entity.Session;
import com.assistant.doitou.repository.ConversationRepository;
import com.assistant.doitou.repository.SessionRepository;
import com.assistant.doitou.repository.UnknownQuestionRepository;
import com.assistant.doitou.repository.UserContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UnknownQuestionRepository unknownQuestionRepository;

    /**
     * Get overall metrics summary
     */
    public OverallMetrics getOverallMetrics() {
        Long totalUsers = sessionRepository.count();
        Long totalConversations = conversationRepository.count();
        Long totalMessages = conversationRepository.countUserMessages();
        Long emailCaptures = userContactRepository.count();
        Long unknownQuestions = unknownQuestionRepository.count();
        Double averageSessionLength = calculateAverageSessionLength();

        return new OverallMetrics(
                totalUsers,
                totalConversations,
                totalMessages,
                emailCaptures,
                unknownQuestions,
                averageSessionLength
        );
    }

    /**
     * Get daily active users for the last N days
     */
    public List<DailyMetric> getDailyActiveUsers(int days) {
        List<DailyMetric> metrics = new ArrayList<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Long activeUsers = sessionRepository.countActiveSessionsByDate(date);
            Long totalMessages = conversationRepository.countMessagesByDate(date);

            metrics.add(new DailyMetric(date, activeUsers, totalMessages));
        }

        return metrics;
    }

    /**
     * Get top N most frequently asked questions
     */
    public List<QuestionFrequency> getTopQuestions(int limit) {
        List<String> allQuestions = conversationRepository.getAllUserQuestions();

        // Count frequency of each question
        Map<String, Long> frequencyMap = allQuestions.stream()
                .filter(q -> q != null && !q.trim().isEmpty())
                .collect(Collectors.groupingBy(
                        q -> q.toLowerCase().trim(),
                        Collectors.counting()
                ));

        // Sort by frequency and return top N
        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> new QuestionFrequency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Get conversion rate (emails captured / total users)
     */
    public ConversionMetrics getConversionRate() {
        Long totalUsers = sessionRepository.count();
        Long emailCaptures = userContactRepository.count();

        Double conversionRate = totalUsers > 0
                ? (emailCaptures.doubleValue() / totalUsers.doubleValue()) * 100
                : 0.0;

        return new ConversionMetrics(totalUsers, emailCaptures, conversionRate);
    }

    /**
     * Calculate average session length in minutes
     */
    private Double calculateAverageSessionLength() {
        List<Session> allSessions = sessionRepository.findAll();

        if (allSessions.isEmpty()) {
            return 0.0;
        }

        double totalMinutes = allSessions.stream()
                .filter(s -> s.getFirstSeen() != null && s.getLastActivity() != null)
                .mapToDouble(s -> {
                    Duration duration = Duration.between(s.getFirstSeen(), s.getLastActivity());
                    return duration.toMinutes();
                })
                .sum();

        return totalMinutes / allSessions.size();
    }

    /**
     * Get engagement metrics for a specific time period
     */
    public Map<String, Object> getEngagementMetrics(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        Long newUsers = sessionRepository.countByFirstSeenAfter(startDate);
        List<Session> recentSessions = sessionRepository.findSessionsSince(startDate);
        List<Conversation> recentConversations = conversationRepository.findConversationsSince(startDate);

        Double avgMessagesPerSession = recentSessions.isEmpty() ? 0.0 :
                recentSessions.stream()
                        .mapToInt(Session::getMessageCount)
                        .average()
                        .orElse(0.0);

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("newUsers", newUsers);
        metrics.put("totalSessions", (long) recentSessions.size());
        metrics.put("totalConversations", (long) recentConversations.size());
        metrics.put("averageMessagesPerSession", avgMessagesPerSession);

        return metrics;
    }
}