package com.example.Transaction.strategy;

import com.example.Transaction.Model.PaymentDetails;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Repository.PaymentDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component("cash")
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy{
    private final PaymentDetailsRepository detailsRepo;

    @Override
    public void charge(Transaction txn) {
        detailsRepo.save(
                PaymentDetails.builder()
                        .transactionId(txn.getId())
                        .provider("cash")
                        .statusCode("succeeded")
                        .paidAt(Instant.now())
                        .rawResponse("{\"method\":\"cash\"}")
                        .build()
        );
    }
}
