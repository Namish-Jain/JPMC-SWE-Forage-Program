package com.jpmc.midascore.controller;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/incentive")
public class IncentiveController {
    private static final Logger logger = LoggerFactory.getLogger(IncentiveController.class);

    @PostMapping
    public Incentive calculateIncentive(@RequestBody Transaction transaction) {
        float incentiveAmount = transaction.getAmount() * 0.1f;
        logger.info("Calculating incentive for transaction: {}, amount: {}", transaction, incentiveAmount);
        return new Incentive(incentiveAmount);
    }
}