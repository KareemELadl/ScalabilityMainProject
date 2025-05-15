package com.example.Transaction.event;


import com.example.Transaction.Model.Transaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransactionFailedEvent extends ApplicationEvent {
    private final Exception cause;

    public TransactionFailedEvent(Transaction txn, Exception cause) {
        super(txn);
        this.cause = cause;
    }

    public Transaction getTransaction() {
        return (Transaction) getSource();
    }
}
