package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveService incentiveService;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (!validateTransaction(transaction, sender, recipient)) {
            return false;
        }

        float incentive = incentiveService.getIncentive(transaction).getAmount();
        logger.info("Processing transaction: {} with incentive: {}", transaction, incentive);

        updateBalances(sender, recipient, transaction, incentive);
        saveTransactionRecord(sender, recipient, transaction, incentive);

        logger.info("Transaction completed successfully");
        return true;
    }

    private boolean validateTransaction(Transaction transaction, UserRecord sender, UserRecord recipient) {
        if (sender == null || recipient == null) {
            logger.warn("Invalid transaction: sender or recipient not found. Transaction: {}", transaction);
            return false;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Invalid transaction: insufficient funds. Sender balance: {}, Amount: {}",
                    sender.getBalance(), transaction.getAmount());
            return false;
        }

        return true;
    }

    private void updateBalances(UserRecord sender, UserRecord recipient, Transaction transaction, float incentive) {
        float senderNewBalance = sender.getBalance() - transaction.getAmount();
        float recipientNewBalance = recipient.getBalance() + transaction.getAmount() + incentive;

        sender.setBalance(senderNewBalance);
        recipient.setBalance(recipientNewBalance);

        userRepository.save(sender);
        userRepository.save(recipient);

        logger.info("Updated balances - Sender {}: {}, Recipient {}: {}",
                sender.getName(), senderNewBalance, recipient.getName(), recipientNewBalance);
    }

    private void saveTransactionRecord(UserRecord sender, UserRecord recipient,
                                       Transaction transaction, float incentive) {
        TransactionRecord transactionRecord = new TransactionRecord(
                sender.getId(),
                recipient.getId(),
                transaction.getAmount(),
                incentive
        );
        transactionRepository.save(transactionRecord);
        logger.info("Saved transaction record: {}", transactionRecord);
    }
}