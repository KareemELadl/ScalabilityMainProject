package com.example.Transaction.strategy;

import com.example.Transaction.Model.PaymentDetails;
import com.example.Transaction.Model.StripeResponse;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.productRequest;
import com.example.Transaction.Repository.PaymentDetailsRepository;
import com.example.Transaction.Service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component("visa")
@RequiredArgsConstructor
public class VisaPaymentStrategy implements PaymentStrategy {

    private final StripeService stripeService;
    private final PaymentDetailsRepository detailsRepo;

    @Override
    @Transactional
    public void charge(Transaction txn) {

        /* Build one “line item” from the txn amount.
           In real life map each TransactionProduct row instead. */
        productRequest req = new productRequest(
                txn.getTotalAmount()
                        .multiply(BigDecimal.valueOf(100))    // to cents
                        .longValue(),
                "Order-" + txn.getId(),
                "usd",
                1L
        );

        StripeResponse s = stripeService.createCheckoutSession(req, txn.getId());

        detailsRepo.save(
                PaymentDetails.builder()
                        .transactionId(txn.getId())
                        .provider("visa")
                        .statusCode("PENDING")                // updated by webhook
                        .paidAt(null)
                        .rawResponse("""
                                     {"sessionId":"%s",
                                      "sessionUrl":"%s"}
                                     """.formatted(s.getSessionId(), s.getSessionUrl()))
                        .build()
        );
    }
}
