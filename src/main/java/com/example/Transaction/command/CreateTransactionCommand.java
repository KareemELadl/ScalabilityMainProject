package com.example.Transaction.command;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.event.TransactionCreatedEvent;
import com.example.Transaction.state.TransactionStateContext;
import com.example.Transaction.strategy.PaymentStrategyFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;


public class CreateTransactionCommand implements TransactionCommand {
    private final Transaction txn;
    private final String provider;
    private final TransactionRepository repo;
    private final PaymentStrategyFactory factory;
    private final TransactionStateContext stateContext;
    private final ApplicationEventPublisher publisher;

    public CreateTransactionCommand(Transaction txn,
                                    String provider,
                                    TransactionRepository repo,
                                    PaymentStrategyFactory factory,
                                    TransactionStateContext stateContext,
                                    ApplicationEventPublisher publisher) {
        this.txn = txn;
        this.provider = provider;
        this.repo = repo;
        this.factory = factory;
        this.stateContext = stateContext;
        this.publisher = publisher;
    }

    @Override
    public Transaction execute() {
        stateContext.transition(txn, TransactionStatus.PENDING);
        txn.setCreatedAt(Instant.now());
        txn.setUpdatedAt(Instant.now());
        repo.save(txn);

        factory.getStrategy(provider).charge(txn);

        stateContext.transition(txn, TransactionStatus.COMPLETED);
        txn.setUpdatedAt(Instant.now());
        repo.save(txn);

        publisher.publishEvent(new TransactionCreatedEvent(txn));
        return txn;
    }
}
