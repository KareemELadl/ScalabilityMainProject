package com.example.Transaction.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategies;
public PaymentStrategyFactory(Map<String, PaymentStrategy> strategies) {
    this.strategies = strategies;
}

public PaymentStrategy getStrategy(String provider) {
    if (provider == null) {
        throw new IllegalArgumentException("Provider cannot be null");
    }
    PaymentStrategy strategy = strategies.get(provider.toLowerCase());
    if (strategy == null) {
        throw new IllegalArgumentException(
                "No payment strategy for provider: " + provider
        );
    }
    return strategy;
}
}