package com.example.Transaction.Controller;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Service.TokenClient;
import com.example.Transaction.Service.TransactionService;
import com.example.Transaction.dto.CheckoutResponse;
import com.example.Transaction.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final TransactionService txnService;
    private final PaymentStrategyFactory strategyFactory;


    @Value("${app.domain-url}")
    private String domainUrl;


    @Autowired
    private TokenClient tokenClient;

    @PostMapping("/{provider}")
    public ResponseEntity<CheckoutResponse> buy(
            @RequestHeader("Authorization") String token,
            @PathVariable String provider,
            @RequestBody Transaction txn
    ) throws Exception {

        System.out.println("YAaaaaa33mmmmmm");
        boolean valid = tokenClient.isTokenValid(token);
        if (!valid) {
            System.out.println("89898998989");
            return ResponseEntity.status(401).build();
        }

        System.out.println("hjosfjsjofsdiofuidvdj");
        Transaction saved = txnService.createTransaction(txn, provider);

        CheckoutResponse result = strategyFactory
                .getStrategy(provider)
                .charge(saved);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/success")
    public String success() { return "Payment succeeded!"; }

    @GetMapping("/cancel")
    public String cancel () { return "Payment cancelled."; }
}