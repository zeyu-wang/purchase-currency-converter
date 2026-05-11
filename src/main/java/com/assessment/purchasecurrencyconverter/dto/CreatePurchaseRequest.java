package com.assessment.purchasecurrencyconverter.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePurchaseRequest(
        @NotBlank
        @Size(max = 50)
        String description,

        @NotNull
        LocalDate transactionDate,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal purchaseAmountUsd
) {
}