package com.jpmc.midascore.foundation;

import com.jpmc.midascore.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class TransactionKafkaListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionKafkaListener.class);

    private final TransactionService transactionService;

    @Value("${general.kafka-topic}")
    private String topic;

    public TransactionKafkaListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            logger.info("Received transaction from topic {}: {}", topic, transaction);
            boolean success = transactionService.processTransaction(transaction);
            if (!success) {
                logger.warn("Transaction processing failed: {}", transaction);
            }
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", transaction, e);
        }
    }
}
