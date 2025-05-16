package com.example.Transaction.Controller;
import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;


    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
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
                                                    @RequestParam TransactionStatus status) throws Exception {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Exception {

        service.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam UUID userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
    {
        if(startDate.after(endDate))
        {
            return ResponseEntity.badRequest().body("Start date must be before end date");
        }
        try
        {
            List<Transaction> transactions = service.filterByDate(userId, startDate, endDate);
            return ResponseEntity.ok(transactions);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
