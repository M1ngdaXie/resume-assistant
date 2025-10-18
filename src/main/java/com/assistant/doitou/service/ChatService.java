package com.assistant.doitou.service;


import com.assistant.doitou.entity.Conversation;
import com.assistant.doitou.entity.Session;
import com.assistant.doitou.entity.UnknownQuestion;
import com.assistant.doitou.entity.UserContact;
import com.assistant.doitou.repository.ConversationRepository;
import com.assistant.doitou.repository.SessionRepository;
import com.assistant.doitou.repository.UnknownQuestionRepository;
import com.assistant.doitou.repository.UserContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UnknownQuestionRepository unknownQuestionRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Transactional
    public Conversation saveConversation(String userId, String role, String message) {
        // Update or create session
        Session session = sessionRepository.findById(userId)
                .orElse(new Session(userId));
        session.incrementMessageCount();
        session.setLastActivity(LocalDateTime.now());
        sessionRepository.save(session);

        // Save conversation
        Conversation conversation = new Conversation(userId, role, message);
        return conversationRepository.save(conversation);
    }

    public List<Conversation> getConversationHistory(String userId) {
        return conversationRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }

    public List<Conversation> getRecentConversations(int limit) {
        return conversationRepository.findTop100ByOrderByCreatedAtDesc();
    }

    @Transactional
    public UserContact saveContact(String email, String name, String notes) {
        // Check if contact already exists
        return userContactRepository.findByEmail(email)
            .map(existing -> {
                // Update existing contact
                if (name != null && !name.isEmpty()) {
                    existing.setName(name);
                }
                if (notes != null && !notes.isEmpty()) {
                    existing.setNotes(notes);
                }
                return userContactRepository.save(existing);
            })
            .orElseGet(() -> {
                // Create new contact
                UserContact newContact = new UserContact(email, name, notes);
                return userContactRepository.save(newContact);
            });
    }

    public List<UserContact> getAllContacts() {
        return userContactRepository.findAll();
    }

    @Transactional
    public UnknownQuestion saveUnknownQuestion(String question) {
        UnknownQuestion unknownQuestion = new UnknownQuestion(question);
        return unknownQuestionRepository.save(unknownQuestion);
    }

    public List<UnknownQuestion> getRecentUnknownQuestions() {
        return unknownQuestionRepository.findTop50ByOrderByCreatedAtDesc();
    }




}