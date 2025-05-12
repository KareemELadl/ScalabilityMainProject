package com.example.Transaction.Controller;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction txn,
                                              @RequestParam String provider) {
        Transaction created = service.createTransaction(txn, provider);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/")
    public ResponseEntity<List<Transaction>> getAll() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getTransaction(id));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getByUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(service.getUserTransactions(userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Transaction> updateStatus(@PathVariable UUID id,
                                                    @RequestParam TransactionStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/items")
    public ResponseEntity<Transaction> addItems(
            @PathVariable UUID id,
            @RequestBody List<String> items) {
        return ResponseEntity.ok(service.addItems(id, items));
    }
}
