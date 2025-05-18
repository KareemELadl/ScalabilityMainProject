//package com.example.Transaction.Service;
//
//import com.example.Transaction.Model.StripeResponse;
//import com.example.Transaction.Model.productRequest;
//import com.stripe.Stripe;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StripeService {
//    @Value("${stripe.secret-key}")
//    private String secretKey;
//    public StripeResponse checkoutProducts(productRequest productRequest ) {
//
//
//        Stripe.apiKey = secretKey;
//        SessionCreateParams.LineItem.PriceData.ProductData productData=SessionCreateParams.LineItem.PriceData.ProductData.builder().
//                setName(productRequest.getName()).build();
//        SessionCreateParams.LineItem.PriceData priceData=SessionCreateParams.LineItem.PriceData.builder().
//                setCurrency("usd").
//                setUnitAmount(productRequest.getAmount()).
//                setProductData(productData).build();
//        SessionCreateParams.LineItem lineItem=SessionCreateParams.LineItem.builder().
//                setPriceData(priceData).
//                setQuantity(productRequest.getQuantity()).build();
//        SessionCreateParams params=SessionCreateParams.builder().
//                addLineItem(lineItem).
//                setMode(SessionCreateParams.Mode.PAYMENT).
//                setSuccessUrl("http://localhost:8080/success").
//                setCancelUrl("http://localhost:8080/cancel").build();
//        Session session;
//        try {
//            session = Session.create(params);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//
//
//return StripeResponse.builder()
//        .status("SUCCESS")
//        .message("Payment Created")
//        .sessionId(session.getId())
//        .sessionUrl(session.getUrl())
//
//        .build();
//    }
//}
package com.example.Transaction.Service;

import com.example.Transaction.Model.StripeResponse;
import com.example.Transaction.Model.productRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    public StripeResponse createCheckoutSession(productRequest req,
                                                Long txnId) {

        Stripe.apiKey = secretKey;                    // initialise once per JVM

        SessionCreateParams.LineItem item =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(req.getQuantity())
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency(req.getCurrency())
                                        .setUnitAmount(req.getAmount())      // **cents/piasters**
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(req.getName())
                                                        .build())
                                        .build())
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addLineItem(item)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .putMetadata("transactionId", txnId.toString())      // link back
                        .setSuccessUrl("http://localhost:8080/checkout/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl ("http://localhost:8080/checkout/cancel")
                        .build();

        try {
            Session session = Session.create(params);
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Checkout session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException e) {
            throw new IllegalStateException("Stripe error - " + e.getMessage(), e);
        }
    }
}

