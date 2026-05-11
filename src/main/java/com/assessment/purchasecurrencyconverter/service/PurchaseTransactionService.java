package com.assessment.purchasecurrencyconverter.service;


import com.assessment.purchasecurrencyconverter.dto.*;
import com.assessment.purchasecurrencyconverter.entity.PurchaseTransaction;
import com.assessment.purchasecurrencyconverter.exception.NotFoundException;
import com.assessment.purchasecurrencyconverter.repository.PurchaseTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class PurchaseTransactionService {

    private final PurchaseTransactionRepository repository;
    private final TreasuryExchangeRateClient treasuryClient;

    public PurchaseTransactionService(
            PurchaseTransactionRepository repository,
            TreasuryExchangeRateClient treasuryClient
    ) {
        this.repository = repository;
        this.treasuryClient = treasuryClient;
    }

    public PurchaseResponse create(CreatePurchaseRequest request) {
        BigDecimal roundedAmount = request.purchaseAmountUsd()
                .setScale(2, RoundingMode.HALF_UP);

        PurchaseTransaction transaction = new PurchaseTransaction(
                request.description(),
                request.transactionDate(),
                roundedAmount
        );

        PurchaseTransaction saved = repository.save(transaction);

        return new PurchaseResponse(
                saved.getId(),
                saved.getDescription(),
                saved.getTransactionDate(),
                saved.getPurchaseAmountUsd()
        );
    }

    public ConvertedPurchaseResponse convert(UUID id, String targetCurrency) {
        PurchaseTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase transaction not found."));

        TreasuryExchangeRateClient.ExchangeRateResult rate =
                treasuryClient.findExchangeRate(targetCurrency, transaction.getTransactionDate());

        BigDecimal convertedAmount = transaction.getPurchaseAmountUsd()
                .multiply(rate.exchangeRate())
                .setScale(2, RoundingMode.HALF_UP);

        return new ConvertedPurchaseResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getPurchaseAmountUsd(),
                rate.currency(),
                rate.exchangeRate(),
                rate.exchangeRateDate(),
                convertedAmount
        );
    }
}