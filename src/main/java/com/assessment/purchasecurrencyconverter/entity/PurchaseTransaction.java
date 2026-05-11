package com.assessment.purchasecurrencyconverter.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class PurchaseTransaction {

    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal purchaseAmountUsd;

    protected PurchaseTransaction() {
    }

    public PurchaseTransaction(String description, LocalDate transactionDate, BigDecimal purchaseAmountUsd) {
        this.id = UUID.randomUUID();
        this.description = description;
        this.transactionDate = transactionDate;
        this.purchaseAmountUsd = purchaseAmountUsd;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getPurchaseAmountUsd() {
        return purchaseAmountUsd;
    }
}