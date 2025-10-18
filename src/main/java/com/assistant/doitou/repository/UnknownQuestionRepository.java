package com.assistant.doitou.repository;

import com.assistant.doitou.entity.UnknownQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnknownQuestionRepository extends JpaRepository<UnknownQuestion, Long> {
    List<UnknownQuestion> findTop50ByOrderByCreatedAtDesc(); // Last 50 unknown questions
}