package com.example.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

     @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        // Validate username and password
        if (account.getUsername().isBlank()) {
            return ResponseEntity.status(400).body(null);
        }
        
        if (account.getPassword().length() < 4) {
            return ResponseEntity.status(400).body(null);
        }

        // Check if username already exists
        if (accountService.existsByUsername(account.getUsername())) {
            return ResponseEntity.status(409).body(null);
        }

        Account savedAccount = accountService.register(account);
        return ResponseEntity.status(200).body(savedAccount);
    }


    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account loginDetails) {
        // Validate that the username and password are provided
        if (loginDetails.getUsername() == null || loginDetails.getPassword() == null) {
            return ResponseEntity.status(400).body(null);
        }

        // Check if account exists and the password matches
        Account account = accountService.authenticate(loginDetails.getUsername(), loginDetails.getPassword());

        if (account != null) {
            return ResponseEntity.status(200).body(account);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    // Endpoint to create a new message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        // Create the message using the MessageService
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage == null) {
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.status(200).body(createdMessage);
    }

     // Endpoint to retrieve all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }
    

    // Endpoint to retrieve a message by id
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);

        if(message != null) {
            return ResponseEntity.status(200).body(message);
        }

        else {
            return ResponseEntity.status(200).body(null);
        }
    }

    // Endpoint to delete a message by id
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        // Attempt to delete the message
        int rowsDeleted = messageService.deleteMessageById(messageId);
        if (rowsDeleted == 0) {
            return ResponseEntity.status(200).body(null);
        }

        return ResponseEntity.status(200).body(rowsDeleted);
    }

    // Endpoint to update a message by id
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<String> updateMessageText(@PathVariable Integer messageId, @RequestBody Map<String, String> requestBody) {
        String newMessageText = requestBody.get("messageText");

        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return ResponseEntity.status(400).body("Invalid message text.");
        }

        // Attempt to update the message
        int rowsUpdated = messageService.updateMessageText(messageId, newMessageText);

        if (rowsUpdated == 1) {
            return ResponseEntity.status(200).body("1");  // 1 row updated
        } else {
            return ResponseEntity.status(400).body("Message not found or invalid message text.");
        }
    }

    // Endpoint to retrieve messages by account id
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
    
    List<Message> messages = messageService.getMessagesByAccountId(accountId);

    return ResponseEntity.status(200).body(messages);   
    
    }

}
