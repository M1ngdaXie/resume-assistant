package com.assistant.doitou.controller;


import com.assistant.doitou.dto.ApiResponse;
import com.assistant.doitou.dto.ContactRequest;
import com.assistant.doitou.entity.UserContact;
import com.assistant.doitou.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<UserContact>> saveContact(@RequestBody ContactRequest request) {
        try {
            UserContact contact = chatService.saveContact(
                request.getEmail(),
                request.getName(),
                request.getNotes()
            );
            return ResponseEntity.ok(ApiResponse.success("Contact saved successfully", contact));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to save contact: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserContact>>> getAllContacts() {
        try {
            List<UserContact> contacts = chatService.getAllContacts();
            return ResponseEntity.ok(ApiResponse.success(contacts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve contacts: " + e.getMessage()));
        }
    }
}