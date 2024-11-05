package com.example.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Create a new message
    public Message createMessage(Message message) {
        // Check if the message text is valid
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return null; // Invalid message text
        }

        // Check if the user who posted the message exists
        Account account = accountRepository.findById(message.getPostedBy()).orElse(null);
        if (account == null) {
            return null; // User not found
        }

        // Save the message to the database
        return messageRepository.save(message);
    }

     // Retrieve all messages
     public List<Message> getAllMessages() {
        return messageRepository.findAll();  // Fetch all messages
    }

     // Retrieve a message by ID
     public Message getMessageById(Integer messageId) {
        return messageRepository.findById(messageId).orElse(null);  // Return null if not found
    }

    // Delete a message by ID
    public int deleteMessageById(Integer messageId) {
        // Check if the message exists
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;  // Successfully deleted 1 row
        }
        return 0;  // No message found to delete
    }

    // Update a message by ID
    public int updateMessageText(Integer messageId, String newMessageText) {
        if(newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return 0;
        }

        Message message = messageRepository.findById(messageId).orElse(null);

        if (message == null) {
            return 0;
        }

        message.setMessageText(newMessageText);
        messageRepository.save(message);

        return 1;
    }

     // Retrieve all messages by accountId
     public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
