package com.example.Transaction.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentStrategyFactory {
    private final Map<String, PaymentStrategy> strategies;

    @Autowired
    public PaymentStrategyFactory(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strat -> {
                            Component annotation = strat.getClass().getAnnotation(Component.class);
                            return annotation != null && !annotation.value().isEmpty()
                                    ? annotation.value() : strat.getClass().getSimpleName().toLowerCase();
                        },
                        strat -> strat
                ));
    }

    public PaymentStrategy getStrategy(String provider) {
        PaymentStrategy strategy = strategies.get(provider);
        if (strategy == null) {
            throw new IllegalArgumentException("No payment strategy for provider: " + provider);
        }
        return strategy;
    }
}
