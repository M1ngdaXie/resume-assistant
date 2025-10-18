package com.assistant.doitou.repository;

import com.assistant.doitou.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserIdOrderByCreatedAtAsc(String userId);
    List<Conversation> findTop100ByOrderByCreatedAtDesc(); // Last 100 conversations
}