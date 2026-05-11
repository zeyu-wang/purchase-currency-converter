package com.assessment.purchasecurrencyconverter.service;

import com.assessment.purchasecurrencyconverter.exception.ConversionUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TreasuryExchangeRateClient {

    private final WebClient webClient;

    public TreasuryExchangeRateClient(
            WebClient.Builder builder,
            @Value("${treasury.api.base-url}") String baseUrl
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public ExchangeRateResult findExchangeRate(String targetCurrency, LocalDate purchaseDate) {
        LocalDate earliestDate = purchaseDate.minusMonths(6);

        TreasuryApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/accounting/od/rates_of_exchange")
                        .queryParam("fields", "country_currency_desc,exchange_rate,record_date")
                        .queryParam("filter",
                                "country_currency_desc:eq:" + targetCurrency +
                                        ",record_date:gte:" + earliestDate +
                                        ",record_date:lte:" + purchaseDate)
                        .queryParam("sort", "-record_date")
                        .queryParam("page[size]", "1")
                        .build())
                .retrieve()
                .bodyToMono(TreasuryApiResponse.class)
                .block();

        if (response == null || response.data() == null || response.data().isEmpty()) {
            throw new ConversionUnavailableException(
                    "The purchase cannot be converted to the target currency because no exchange rate is available within 6 months equal to or before the purchase date."
            );
        }

        TreasuryRate rate = response.data().getFirst();

        return new ExchangeRateResult(
                rate.country_currency_desc(),
                new BigDecimal(rate.exchange_rate().replace(",", "")),
                LocalDate.parse(rate.record_date())
        );
    }

    public record ExchangeRateResult(
            String currency,
            BigDecimal exchangeRate,
            LocalDate exchangeRateDate
    ) {
    }

    public record TreasuryApiResponse(List<TreasuryRate> data) {
    }

    public record TreasuryRate(
            String country_currency_desc,
            String exchange_rate,
            String record_date
    ) {
    }
}