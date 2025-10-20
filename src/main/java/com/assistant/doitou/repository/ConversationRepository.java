package com.assistant.doitou.repository;

import com.assistant.doitou.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserIdOrderByCreatedAtAsc(String userId);
    List<Conversation> findTop100ByOrderByCreatedAtDesc(); // Last 100 conversations

    // Analytics queries
    @Query("SELECT c.message FROM Conversation c WHERE c.role = 'user' ORDER BY c.createdAt DESC")
    List<String> getAllUserQuestions();

    @Query("SELECT COUNT(c) FROM Conversation c WHERE c.role = 'user'")
    Long countUserMessages();

    @Query("SELECT COUNT(c) FROM Conversation c WHERE DATE(c.createdAt) = :date")
    Long countMessagesByDate(@Param("date") java.time.LocalDate date);

    @Query("SELECT c FROM Conversation c WHERE c.createdAt >= :startDate ORDER BY c.createdAt ASC")
    List<Conversation> findConversationsSince(@Param("startDate") LocalDateTime startDate);
}