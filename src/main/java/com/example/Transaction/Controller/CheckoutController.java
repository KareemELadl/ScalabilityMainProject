package com.example.Transaction.Controller;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final TransactionService txnService;

    @PostMapping("/{provider}")
    public ResponseEntity<Transaction> buy(
            @PathVariable String provider,
            @RequestBody Transaction txn
    ) throws Exception {
        Transaction created = txnService.createTransaction(txn, provider);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/success")
    public String success() { return "Payment succeeded!"; }

    @GetMapping("/cancel")
    public String cancel () { return "Payment cancelled."; }
}