package com.example.Transaction.Service;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    Transaction createTransaction(Transaction txn, String provider);
    Transaction getTransaction(UUID transactionId);

    List<Transaction> getAllTransactions();

    List<Transaction> getUserTransactions(UUID userId);
    Transaction updateStatus(UUID transactionId, TransactionStatus status);
    void deleteTransaction(UUID transactionId);

    @Transactional
    Transaction addItems(UUID transactionId, List<String> items);
}
