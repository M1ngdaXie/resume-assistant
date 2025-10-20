package com.assistant.doitou.repository;

import com.assistant.doitou.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    List<Session> findByLastActivityAfter(LocalDateTime dateTime);
    Long countByFirstSeenAfter(LocalDateTime dateTime);

    // Analytics queries
    @Query("SELECT AVG(s.messageCount) FROM Session s WHERE s.messageCount > 0")
    Double getAverageMessageCount();

    @Query("SELECT s FROM Session s WHERE s.firstSeen >= :startDate ORDER BY s.firstSeen ASC")
    List<Session> findSessionsSince(@Param("startDate") LocalDateTime startDate);

    // Count sessions active on a specific date
    @Query("SELECT COUNT(DISTINCT s.sessionId) FROM Session s WHERE DATE(s.lastActivity) = :date")
    Long countActiveSessionsByDate(@Param("date") java.time.LocalDate date);
}
