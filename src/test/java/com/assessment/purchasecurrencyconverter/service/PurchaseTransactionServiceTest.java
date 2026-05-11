package com.assessment.purchasecurrencyconverter.service;


import com.assessment.purchasecurrencyconverter.dto.CreatePurchaseRequest;
import com.assessment.purchasecurrencyconverter.dto.PurchaseResponse;
import com.assessment.purchasecurrencyconverter.repository.PurchaseTransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseTransactionServiceTest {

   @Test
   void shouldRoundPurchaseAmountToNearestCent() {
       PurchaseTransactionRepository repository = Mockito.mock(PurchaseTransactionRepository.class);
       TreasuryExchangeRateClient treasuryClient = Mockito.mock(TreasuryExchangeRateClient.class);

       Mockito.when(repository.save(Mockito.any()))
               .thenAnswer(invocation -> invocation.getArgument(0));

       PurchaseTransactionService service = new PurchaseTransactionService(repository, treasuryClient);

       PurchaseResponse response = service.create(new CreatePurchaseRequest(
               "Laptop",
               LocalDate.of(2025, 12, 15),
               new BigDecimal("123.456")
       ));

       assertThat(response.purchaseAmountUsd()).isEqualByComparingTo("123.46");
   }
}