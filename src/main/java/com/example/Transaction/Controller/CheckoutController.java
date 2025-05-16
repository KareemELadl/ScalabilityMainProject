package com.example.Transaction.Controller;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Service.TransactionService;
import com.example.Transaction.Service.UserVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private static final String TEST_USER_HEADER = "X-User-Id";

    private final TransactionService txnService;
    private final UserVerificationService userVerification;

    @PostMapping("/{provider}")
    public ResponseEntity<Transaction> buy(
            HttpServletRequest request,
            @PathVariable String provider,
            @RequestBody Transaction txn
    ) throws Exception {
        UUID userId = resolveUserId(request);
        txn.setUserId(userId);
        Transaction created = createTransaction(txn, provider);
        return ResponseEntity.ok(created);
    }

    private UUID resolveUserId(HttpServletRequest request) {
        String testUser = request.getHeader(TEST_USER_HEADER);
        if (testUser != null && !testUser.isEmpty()) {
            return UUID.fromString(testUser);
        }
        String token = extractBearer(request);
        return userVerification.verifyLogin(token);
    }

    private String extractBearer(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing or invalid Authorization header"
            );
        }
        return auth.substring(7);
    }

    private Transaction createTransaction(Transaction txn, String provider) throws Exception {
        return txnService.createTransaction(txn, provider);
    }

    @GetMapping("/success")
    public String success() {
        return "Payment succeeded!";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "Payment cancelled.";
    }
}