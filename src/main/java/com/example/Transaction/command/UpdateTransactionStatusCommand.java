package com.example.Transaction.command;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.state.TransactionStateContext;
import java.time.Instant;
import java.util.UUID;


public class UpdateTransactionStatusCommand implements TransactionCommand {
    private final TransactionRepository repo;
    private final TransactionStateContext stateContext;
    private final UUID transactionId;
    private final TransactionStatus newStatus;

    public UpdateTransactionStatusCommand(TransactionRepository repo,
                                          TransactionStateContext stateContext,
                                          UUID transactionId,
                                          TransactionStatus newStatus) {
        this.repo = repo;
        this.stateContext = stateContext;
        this.transactionId = transactionId;
        this.newStatus = newStatus;
    }

    @Override
    public Transaction execute() {
        Transaction txn = repo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        stateContext.transition(txn, newStatus);
        txn.setUpdatedAt(Instant.now());
        return repo.save(txn);
    }
}