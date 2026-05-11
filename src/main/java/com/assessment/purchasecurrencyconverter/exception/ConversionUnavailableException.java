package com.assessment.purchasecurrencyconverter.exception;

public class ConversionUnavailableException extends RuntimeException {
    public ConversionUnavailableException(String message) {
        super(message);
    }
}