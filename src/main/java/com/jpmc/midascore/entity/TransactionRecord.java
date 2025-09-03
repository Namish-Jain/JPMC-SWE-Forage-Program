package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long senderId;

    @Column(nullable = false)
    private long recipientId;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // JPA requires a no-args constructor
    protected TransactionRecord() {}

    public TransactionRecord(long senderId, long recipientId, float amount, float incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }

    public long getSenderId() { return senderId; }

    public long getRecipientId() { return recipientId; }

    public float getAmount() { return amount; }

    public float getIncentive() { return incentive; }

    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", amount=" + amount +
                ", incentive=" + incentive +
                ", timestamp=" + timestamp +
                '}';
    }
}
