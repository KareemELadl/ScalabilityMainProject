package com.example.Transaction.command;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Repository.TransactionRepository;

import java.util.UUID;


public class DeleteTransactionCommand implements TransactionCommand {
    private final TransactionRepository repo;
    private final UUID transactionId;

    public DeleteTransactionCommand(TransactionRepository repo, UUID transactionId) {
        this.repo = repo;
        this.transactionId = transactionId;
    }

    @Override
    public Transaction execute() {
        Transaction txn = repo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        repo.deleteById(transactionId);
        return txn;
    }
}
