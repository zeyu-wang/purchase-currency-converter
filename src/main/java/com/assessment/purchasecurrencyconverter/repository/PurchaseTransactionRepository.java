package com.assessment.purchasecurrencyconverter.repository;

import com.assessment.purchasecurrencyconverter.entity.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, UUID> {
}