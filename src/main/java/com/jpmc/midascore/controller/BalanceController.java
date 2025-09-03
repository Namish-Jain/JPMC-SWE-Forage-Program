package com.jpmc.midascore.controller;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Balance getBalance(@RequestParam Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            logger.warn("User not found with id: {}", userId);
            return new Balance(0.0f);
        }
        logger.info("Retrieved balance for user {}: {}", userId, user.get().getBalance());
        return new Balance(user.get().getBalance());
    }
}