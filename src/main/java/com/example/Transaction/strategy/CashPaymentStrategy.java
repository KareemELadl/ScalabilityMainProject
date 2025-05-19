package com.example.Transaction.strategy;

import com.example.Transaction.dto.CheckoutResponse;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component("cash")
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {
    private final StripeService stripeService;

    @Override
    @Transactional
    public CheckoutResponse charge(Transaction txn) {
        // Delegate persistence to StripeService
        stripeService.recordCashPayment(txn.getId());

        txn.setStatus(TransactionStatus.COMPLETED);
        txn.setUpdatedAt(Instant.now());
        return new CheckoutResponse(txn, null, null);
    }
}