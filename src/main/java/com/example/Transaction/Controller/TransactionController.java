package com.example.Transaction.Controller;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Service.TokenClient;
import com.example.Transaction.Service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final String TEST_USER_HEADER = "X-User-Id";

    private final TransactionService service;

    @Autowired
    private TokenClient tokenClient;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<List<Transaction>> getAll(HttpServletRequest request) {
        checkLogin(request);
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(HttpServletRequest request,
                                               @PathVariable Long id) {
        checkLogin(request);
        return ResponseEntity.ok(service.getTransaction(id));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getByUser(HttpServletRequest request,
                                                       @RequestParam Long userId) {
        checkLogin(request);
        return ResponseEntity.ok(service.getUserTransactions(userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Transaction> updateStatus(HttpServletRequest request,
                                                    @PathVariable Long id,
                                                    @RequestParam TransactionStatus status) throws Exception {
        checkLogin(request);
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(HttpServletRequest request,
                                       @PathVariable Long id) throws Exception {
        checkLogin(request);
        service.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(HttpServletRequest request,
                                    @RequestParam Long userId,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        checkLogin(request);
        if (startDate.after(endDate)) {
            return ResponseEntity.badRequest().body("Start date must be before end date");
        }
        List<Transaction> txns = service.filterByDate(userId, startDate, endDate);
        return ResponseEntity.ok(txns);
    }

    private void checkLogin(HttpServletRequest request) {
//        String testUser = request.getHeader(TEST_USER_HEADER);
//        if (testUser != null && !testUser.isEmpty()) {
//            return;
//        }
        String auth = request.getHeader("Authorization");
        boolean valid = tokenClient.isTokenValid(auth);

        if (!valid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

      //  String token = auth.substring(7);
//        try {
//        //    userVerification.verifyLogin(token);
//        } catch (Exception ex) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
//        }
    }
}
