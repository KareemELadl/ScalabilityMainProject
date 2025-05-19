package com.example.Transaction.Service;

import com.example.Transaction.Model.PaymentDetails;
import com.example.Transaction.Repository.PaymentDetailsRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${app.domain-url}")
    private String domainUrl; // e.g. https://your.domain

    private final PaymentDetailsRepository detailsRepo;

    @PostConstruct
    public void init() {
        com.stripe.Stripe.apiKey = secretKey;
    }

    /**
     * Record a cash payment immediately.
     */
    @Transactional
    public void recordCashPayment(Long txnId) {
        detailsRepo.save(
                PaymentDetails.builder()
                        .transactionId(txnId)
                        .provider("cash")
                        .statusCode("succeeded")
                        .paidAt(Instant.now())
                        .rawResponse("{\"method\":\"cash\"}")
                        .build()
        );
    }

    /**
     * Create a Stripe Checkout Session for multiple line items
     * and persist a PENDING payment details record.
     */
    @Transactional
    public Session createCheckoutSession(
            List<SessionCreateParams.LineItem> items,
            Long txnId
    ) throws StripeException {
        // dynamically build success/cancel URLs
        String successUrl = String.format(
                "%s/checkout/success?session_id={CHECKOUT_SESSION_ID}",
                domainUrl
        );
        String cancelUrl = String.format(
                "%s/checkout/cancel",
                domainUrl
        );

        SessionCreateParams params = SessionCreateParams.builder()
                .addAllLineItem(items)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .putMetadata("transactionId", txnId.toString())
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .build();

        Session session = Session.create(params);

        // persist PENDING state for Visa
        detailsRepo.save(
                PaymentDetails.builder()
                        .transactionId(txnId)
                        .provider("visa")
                        .statusCode("PENDING")
                        .paidAt(null)
                        .rawResponse(String.format(
                                "{\"sessionId\":\"%s\",\"sessionUrl\":\"%s\"}",
                                session.getId(), session.getUrl()
                        ))
                        .build()
        );

        return session;
    }
}