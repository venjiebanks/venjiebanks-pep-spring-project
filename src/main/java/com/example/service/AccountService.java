package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

   // Check if a username already exists in the database
    public boolean existsByUsername(String username) {
        return accountRepository.findByUsername(username).isPresent();
    }

    // Register a new account
    public Account register(Account account) {
        // Save the account (which will auto-generate the accountId)
        return accountRepository.save(account);
    }


    public Account authenticate(String username, String password) {
        // Check if the account exists with the given username
        Account account = accountRepository.findByUsername(username).orElse(null);

        // If account exists and password matches, return the account
        if (account != null && account.getPassword().equals(password)) {
            return account;
        } else {
            return null;  // Return null if authentication fails
        }
    }

}
