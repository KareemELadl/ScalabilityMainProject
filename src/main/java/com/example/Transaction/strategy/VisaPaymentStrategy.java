package com.example.Transaction.strategy;

import com.example.Transaction.dto.CheckoutResponse;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component("visa")
@RequiredArgsConstructor
public class VisaPaymentStrategy implements PaymentStrategy {

    private final StripeService stripeService;

    @Override
    @Transactional
    public CheckoutResponse charge(Transaction txn) throws Exception {
        // build Stripe line items
        List<SessionCreateParams.LineItem> items = txn.getItems().stream()
                .map(item -> SessionCreateParams.LineItem.builder()
                        .setQuantity(item.getQuantity().longValue())
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(item.getProduct().getPrice()
                                                .multiply(BigDecimal.valueOf(100)).longValue())
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build())
                                        .build())
                        .build()
                )
                .collect(Collectors.toList());

        Session session = stripeService.createCheckoutSession(items, txn.getId());

        return new CheckoutResponse(txn, session.getUrl(), session.getId());
    }
}