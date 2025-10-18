package com.assistant.doitou.repository;

import com.assistant.doitou.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    List<Session> findByLastActivityAfter(LocalDateTime dateTime);
    Long countByFirstSeenAfter(LocalDateTime dateTime);
}
