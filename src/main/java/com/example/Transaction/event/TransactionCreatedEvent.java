package com.example.Transaction.event;

import com.example.Transaction.Model.Transaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class TransactionCreatedEvent extends ApplicationEvent {
    private final Transaction transaction;

    public TransactionCreatedEvent(Transaction transaction) {
        super(transaction);
        this.transaction = transaction;
    }

}
