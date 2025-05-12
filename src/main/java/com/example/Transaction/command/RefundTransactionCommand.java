package com.example.Transaction.command;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.event.TransactionRefundedEvent;
import com.example.Transaction.state.TransactionStateContext;
import org.springframework.context.ApplicationEventPublisher;
import java.time.Instant;

public class RefundTransactionCommand implements TransactionCommand {
    private final TransactionRepository repo;
    private final TransactionStateContext stateContext;
    private final ApplicationEventPublisher publisher;
    private final Transaction txn;

    public RefundTransactionCommand(TransactionRepository repo,
                                    TransactionStateContext stateContext,
                                    ApplicationEventPublisher publisher,
                                    Transaction txn) {
        this.repo = repo;
        this.stateContext = stateContext;
        this.publisher = publisher;
        this.txn = txn;
    }

    @Override
    public Transaction execute() {
        stateContext.transition(txn, TransactionStatus.REFUNDED);
        txn.setUpdatedAt(Instant.now());
        Transaction updated = repo.save(txn);
        publisher.publishEvent(new TransactionRefundedEvent(updated));
        return updated;
    }
}

