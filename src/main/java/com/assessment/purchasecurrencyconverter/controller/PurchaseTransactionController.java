package com.assessment.purchasecurrencyconverter.controller;

import com.assessment.purchasecurrencyconverter.dto.*;
import com.assessment.purchasecurrencyconverter.service.PurchaseTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseTransactionController {

    private final PurchaseTransactionService service;

    public PurchaseTransactionController(PurchaseTransactionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseResponse create(@Valid @RequestBody CreatePurchaseRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}/conversion")
    public ConvertedPurchaseResponse convert(
            @PathVariable UUID id,
            @RequestParam String targetCurrency
    ) {
        return service.convert(id, targetCurrency);
    }
}