package com.example.Transaction.strategy;


import com.example.Transaction.Model.PaymentDetails;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Repository.PaymentDetailsRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component("visa")
@RequiredArgsConstructor
public class VisaPaymentStrategy implements PaymentStrategy {
    private final PaymentDetailsRepository detailsRepo;

    @Value("${stripe.secret-key}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    @Override
    public void charge(Transaction txn) {
        try {
            long amount = txn.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValueExact();
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .addPaymentMethodType("card")
                    .setDescription("Transaction " + txn.getId())
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);

            detailsRepo.save(
                    PaymentDetails.builder()
                            .transactionId(txn.getId())
                            .provider("visa")
                            .statusCode(intent.getStatus())
                            .paidAt(Instant.ofEpochSecond(intent.getCreated()))
                            .rawResponse(intent.toJson())
                            .build()
            );

            if (!"succeeded".equals(intent.getStatus())) {
                throw new RuntimeException("Payment failed: " + intent.getStatus());
            }
        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage(), e);
        }
    }
}

